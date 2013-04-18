/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash

import groovy.util.logging.Slf4j

import org.junit.Test

import com.google.inject.Injector

/**
 * Test the build-in command {@code parse}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@Slf4j
class ParseBuildinTest extends ScriptTestUtils {
	
	static validArgs = ["-a", "foo", "-b", "10", "-c", "more", "arguments"]
	
	static notValidArgs = []
	
	static parameterClass = """class Parameter {

    @Option(name = "-a", required = true, usage = "Parameter A")
    String parameterA

    @Option(name = "-b", required = true, usage = "Parameter B")
    int parameterB

    @Option(name = "-c", usage = "Parameter C")
    boolean parameterC

	@Argument
	List<String> arguments
}
"""

	@Test
	void "parse command line arguments with no arguments specified"() {
		def script = """
$parameterClass

def printHelp(def parser) {
    echo "Help: "
	echo parser
}

parser = parse new Parameter()
if (!parser.isValid) printHelp parser
echo parser.parameterA
"""
		runParser script, notValidArgs, { parser ->
			shouldFailWith IllegalStateException, {
				parser.run()	
			}
		}
	}

	@Test
	void "if valid - not valid syntax with valid arguments"() {
		def script = """
$parameterClass
parser = parse new Parameter()
parser.valid {
	echo "Valid"
} notValid {
	echo "Not Valid"
} 
"""
		runParser script, validArgs
		assertStringContent "Valid\n", output
	}

	@Test
	void "if valid - not valid syntax with not valid arguments"() {
		def script = """
$parameterClass
parser = parse new Parameter()
parser.valid {
	echo "Valid"
} notValid {
	echo "Not Valid"
} 
"""
		runParser script, notValidArgs
		assertStringContent "Not Valid\n", output
	}

	@Test
	void "if valid - not valid syntax with not valid arguments and pass parser"() {
		def script = """
$parameterClass

def printHelp(def parser) {
    echo "Help:"
	echo parser != null
}

parse(new Parameter()).valid {
	echo "Valid"
} notValid { parser ->
	printHelp parser
} 
"""
		runParser script, notValidArgs
		assertStringContent "Help:\ntrue\n", output
	}

	@Test
	void "parse command line arguments"() {
		def script = """
$parameterClass
echo ARGS
parser = parse new Parameter()
echo parser.parameterA
echo parser.parameterB
echo parser.parameterC
echo parser.arguments
"""
		runParser script, validArgs
		assertStringContent """[-a, foo, -b, 10, -c, more, arguments]
foo
10
true
[more, arguments]
""", output
	}

	@Test
	void "print example of the command line arguments"() {
		def script = """
$parameterClass
parser = parse new Parameter()
parser.printExample()
echo
parser.printSingleLineUsage()
echo
parser.printUsage()
"""
		runParser script, validArgs
		assertStringContent """ -a VAL -b N -c
 [VAL ...] -a VAL -b N [-c]
 -a VAL : Parameter A
 -b N   : Parameter B
 -c     : Parameter C
""", output
	}

	@Test
	void "get example of the command line arguments"() {
		def script = """
$parameterClass
parser = parse new Parameter()
echo parser.example
echo parser.singleLineUsage
echo parser.usage
"""
		runParser script, validArgs
		assertStringContent """ -a VAL -b N -c
 [VAL ...] -a VAL -b N [-c]
 -a VAL : Parameter A
 -b N   : Parameter B
 -c     : Parameter C

""", output
	}
}

