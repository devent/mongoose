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
package com.anrisoftware.mongoose.buildins.sudobuildin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.buildins.utils.BuildinsTestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.google.inject.Injector

/**
 * @see SudoBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SudoTest {

	@Test
	void "inline"() {
		def string = "Test"
		def file = tmp.newFile()
		write file, string
		shouldFailWith(CommandException) { command.call "cat $file" }
	}

	//@Test
	void "with terminal"() {
		def string = "Test"
		def file = tmp.newFile()
		write file, string
		command terminal: true, "cat $file"
		println output(byteOutput)
	}

	SudoBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	ByteArrayOutputStream byteError

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	@After
	void logErrors() {
		log.info output(byteError)
	}

	static Injector injector

	@Before
	void setupCommand() {
		environment = createEnvironment injector
		command = createCommand injector, environment
		byteOutput = new ByteArrayOutputStream()
		byteError = new ByteArrayOutputStream()
		command.setOutput(byteOutput)
		command.setError(byteError)
	}

	@BeforeClass
	static void setupInjector() {
		injector = createInjector().createChildInjector(new SudoModule())
	}

}
