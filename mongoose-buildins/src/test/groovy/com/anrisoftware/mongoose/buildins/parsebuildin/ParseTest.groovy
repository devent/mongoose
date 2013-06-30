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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.buildins.utils.BuildinsTestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.ExampleMode
import org.kohsuke.args4j.Option

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.google.inject.Injector

/**
 * @see ParseBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseTest {

	@Test(expected = IllegalArgumentException)
	void "no args"() {
		command.args()
		command()
	}

	@Test(expected = CommandException)
	void "no arguments"() {
		command new Parameter()
	}

	@Test
	void "all args"() {
		def a = "str"
		def b = 10
		def c = true
		def args = ["a", "b"]
		environment.setArgs arguments
		command.setEnvironment environment
		command new Parameter()
		assert command.theParameter.parameterA == a
		assert command.theParameter.parameterB == b
		assert command.theParameter.parameterC == c
		assert command.theParameter.arguments.size() == 2
	}

	@Test
	void "set args"() {
		command arguments: arguments, new Parameter()
		assert command.theParameter.parameterA == a
		assert command.theParameter.parameterB == b
		assert command.theParameter.parameterC == c
		assert command.theParameter.arguments.size() == 2
	}

	@Test
	void "valid"() {
		boolean validCalled = false
		def valid = { validCalled = true }
		command arguments: arguments, valid: valid, new Parameter()
		assert validCalled
	}

	@Test
	void "not valid"() {
		boolean notValidCalled = false
		def notValid = { notValidCalled = true }
		try {
			command arguments: [], notValid: notValid, new Parameter()
		} catch (e) {
			assert notValidCalled
		}
	}

	@Test
	void "print example"() {
		command.args new Parameter()
		assert command.printExample(ExampleMode.ALL) == " -a VAL -b N -c"
	}

	@Test
	void "print single line usage"() {
		command.args new Parameter()
		assert command.printSingleLineUsage() == " [VAL ...] -a VAL -b N [-c]"
	}

	@Test
	void "print usage"() {
		command.args new Parameter()
		assert command.printUsage() == """ -a VAL : Parameter A
 -b N   : Parameter B
 -c     : Parameter C
"""
	}

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

	ParseBuildin command

	Environment environment

	static Injector injector

	static a = "str"

	static b = 10

	static c = true

	static args = ["a", "b"]

	static arguments = [
		"-a",
		"$a",
		"-b",
		"$b",
		"-c",
		"${args[0]}",
		"${args[1]}"
	]

	@Before
	void setupCommand() {
		environment = createEnvironment injector
		command = createCommand injector, environment
	}

	@BeforeClass
	static void setupInjector() {
		injector = createInjector().createChildInjector(new ParseModule())
	}
}
