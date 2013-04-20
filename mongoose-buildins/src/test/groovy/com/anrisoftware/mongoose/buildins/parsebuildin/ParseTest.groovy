/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.buildins.parsebuildin

import org.junit.Before
import org.junit.Test
import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.Option

import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.anrisoftware.mongoose.buildins.parsebuildin.ParseBuildin;
import com.anrisoftware.mongoose.buildins.parsebuildin.ParseModule;

/**
 * Test the build-in command {@code parse}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParseTest extends BuildinTestUtils {

	static validArgs = ["-a", "foo", "-b", "10", "-c", "more", "arguments"]
	
	static notValidArgs = ["-x"]

	static example = " -a VAL -b N -c"
	
	static singleLineUsage = " [VAL ...] -a VAL -b N [-c]"
	
	static usage = """ -a VAL : Parameter A
 -b N   : Parameter B
 -c     : Parameter C
"""
	
	static class Parameter {
		
		@Option(name = "-a", required = true, usage = "Parameter A")
		String parameterA
		
		@Option(name = "-b", required = true, usage = "Parameter B")
		int parameterB
		
		@Option(name = "-c", usage = "Parameter C")
		boolean parameterC
		
		@Argument
		List<String> arguments
	}
	
	static assertValidArguments(def result) {
		assert result.isValid == true
		assert result.parameterA == validArgs[1]
		assert result.parameterB == validArgs[3]as int
		assert result.parameterC == true
		assert result.arguments == [validArgs[5], validArgs[6]]
	}

	def assertNotValidArguments(def result) {
		assert result.isValid == false
		shouldFailWith IllegalStateException, {
			assert result.parameterA
		}
	}

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new ParseModule()
	}

	@Test
	void "parse command line arguments with no arguments specified"() {
		def result = createBuildin(ParseBuildin, [new Parameter()])()
		assertNotValidArguments result
	}

	@Test
	void "parse command line arguments with valid arguments"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), validArgs])()
		assertValidArguments result
	}

	@Test
	void "parse command line arguments with valid arguments so result equals true"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), validArgs])()
		assert result == true
	}

	@Test
	void "parse command line arguments with valid arguments access not defined parameter"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), validArgs])()
		shouldFailWith MissingFieldException, {
			assert result.xxx
		}
	}

	@Test
	void "parse command line arguments with not valid arguments"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), notValidArgs])()
		assertNotValidArguments result
	}

	@Test
	void "print example of the command line arguments"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), validArgs])()
		result.printExample()
		result.printSingleLineUsage()
		result.printUsage()
		assertStringContent output, "$example$singleLineUsage$usage"
	}

	@Test
	void "get example of the command line arguments"() {
		def result = createBuildin(ParseBuildin, [new Parameter(), validArgs])()
		assertStringContent result.example, example
		assertStringContent result.singleLineUsage, singleLineUsage
		assertStringContent result.usage, usage
	}
}
