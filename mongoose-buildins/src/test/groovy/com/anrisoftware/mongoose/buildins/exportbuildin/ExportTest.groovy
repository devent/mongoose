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
package com.anrisoftware.mongoose.buildins.exportbuildin

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see ExportBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExportTest {

	@Test
	void "env [print]"() {
		command()
		println output(byteOutput)
	}

	@Test
	void "env [set symbols]"() {
		command "FOO", "BAR"
		assert environment.getEnv()["FOO"] == "1"
		assert environment.getEnv()["BAR"] == "1"
	}

	@Test
	void "env [set value]"() {
		command "FOO": "foo"
		assert environment.getEnv()["FOO"] == "foo"
	}

	@Test
	void "env [remove value]"() {
		def env = environment.getEnv()
		env["FOO"] = "foo"
		environment.setEnv env
		command remove: true, "FOO": "foo"
		assert !environment.getEnv().containsKey("FOO")
	}

	@Test
	void "env [remove symbols]"() {
		def env = environment.getEnv()
		env["FOO"] = "foo"
		env["BAR"] = "bar"
		environment.setEnv env
		command remove: true, "FOO", "BAR"
		assert !environment.getEnv().containsKey("FOO")
		assert !environment.getEnv().containsKey("BAR")
	}

	ExportBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	@Before
	void setupCommand() {
		command = injector.getInstance(ExportBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
		byteOutput = new ByteArrayOutputStream()
		command.setOutput(byteOutput)
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		TestUtils.toStringStyle
		injector = Guice.createInjector(
				new ExportModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
