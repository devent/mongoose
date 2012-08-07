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
package com.anrisoftware.groovybash.core.parser

import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.google.inject.Injector

/**
 * Test the build-in command {@link EchoBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ParseParameterTest extends CommandTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector()
	}

	@Test
	void "parse command line arguments"() {
		def args = ["-a", "foo", "-b", "10", "-c"]
		def script = """
class Parameter {

    @Option(name = "-a", required = true)
    String parameterA

    @Option(name = "-b", required = true)
    int parameterB

    @Option(name = "-c")
    boolean parameterC
}

echo ARGS
def parser = parse new Parameter()
echo parser.parameterA
echo parser.parameterB
echo parser.parameterC
"""
		runParser script, null, args
		log.info output
	}

	@Test
	void "parse command line arguments and print example"() {
		def args = ["-a", "foo", "-b", "10", "-c"]
		def script = """
class Parameter {

    @Option(name = "-a", required = true, usage = "Parameter A")
    String parameterA

    @Option(name = "-b", required = true, usage = "Parameter B")
    int parameterB

    @Option(name = "-c", usage = "Parameter C")
    boolean parameterC
}

parser = parse new Parameter()
parser.printExample()
parser.printSingleLineUsage()
parser.printUsage()
"""
		runParser script, null, args
		log.info output
	}
}

