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
package com.anrisoftware.mongoose.buildins.buildinbuildin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.buildins.utils.BuildinsTestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.environment.Environment
import com.google.inject.Injector

/**
 * @see BuildinBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BuildinTest {

	@Test
	void "delegate property to build-in command"() {
		def str = "Foo"
		def cmd = command name: "listFiles", str
		assert cmd.theFiles == []
	}

	@Test
	void "no args"() {
		shouldFailWith(IllegalArgumentException) { command.args() }
	}

	@Test
	void "unknown build-in command"() {
		shouldFailWith(NullPointerException) { command.call name: "unknown" }
	}

	BuildinBuildin command

	Environment environment

	@Before
	void setupCommand() {
		environment = createEnvironment injector
		command = createCommand injector, environment
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		injector = createInjector().createChildInjector(new BuildinModule())
	}
}
