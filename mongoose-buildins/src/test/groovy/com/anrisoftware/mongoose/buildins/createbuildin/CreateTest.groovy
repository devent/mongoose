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
package com.anrisoftware.mongoose.buildins.createbuildin

import org.apache.commons.io.FileUtils
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
 * @see CreateBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CreateTest {

	@Test
	void "create"() {
		def created = command name: "cd" theCommand
		assert created != null
	}

	@Test(expected = IllegalArgumentException)
	void "create [no args]"() {
		command.args()
		command()
	}

	@Test
	void "create [external command]"() {
		def tmp = File.createTempFile("text", "txt")
		FileUtils.write tmp, "Hello"

		try {
			def cmd = command name: "cat $tmp" theCommand
			cmd()
		} finally {
			tmp.delete()
		}
	}

	CreateBuildin command

	Environment environment

	@Before
	void setupCommand() {
		command = injector.getInstance(CreateBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		TestUtils.toStringStyle
		injector = Guice.createInjector(
				new CreateModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}
}