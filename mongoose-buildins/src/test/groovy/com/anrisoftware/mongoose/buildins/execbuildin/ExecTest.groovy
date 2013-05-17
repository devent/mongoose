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
package com.anrisoftware.mongoose.buildins.execbuildin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see ExecBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecTest {

	@Test
	void "cat file"() {
		def string = "Test"
		def file = tmp.newFile()
		write file, string
		command "cat $file"
		assertStringContent output(byteOutput), string
	}

	@Test
	void "cat file [+ working directory]"() {
		def string = "Test"
		def dir = tmp.newFolder()
		def file = new File(dir, "file")
		write file, string
		command "cat ${file.name}", [:], dir
		assertStringContent output(byteOutput), string
	}

	@Test
	void "bash [+env variable]"() {
		def file = tmp.newFile()
		def string = "Test"
		write file, 'echo -n $VAR'
		command "bash $file", [VAR: string]
		assertStringContent output(byteOutput), string
	}

	@Test(expected = CommandException)
	void "sleep [+timeout]"() {
		command timeout: 1000, "sleep 2"
	}

	@Test
	void "exit value"() {
		def value = 99
		def file = tmp.newFile()
		write file, "exit $value"
		command successExitValue: value, "bash $file"
	}

	@Test
	void "exit values"() {
		def value = 99
		def file = tmp.newFile()
		write file, "exit $value"
		command successExitValues: [value], "bash $file"
	}

	@Test
	void "interactive [+terminal command]"() {
		command terminal: true, terminalCommand: konsoleCmd, "sleep 2"
	}

	@Test
	void "interactive [+default terminal command]"() {
		command terminal: true, "sleep 2"
	}

	@Test
	void "interactive [+default terminal command, +new line]"() {
		command terminal: true, "sleep 2\n"
	}

	@Test
	void "interactive [+default terminal command, error return]"() {
		def cmd = command
		shouldFailWith(CommandException) { cmd terminal: true, "fsck xxx" }
		assert cmd.theExitValue == 16
	}

	ExecBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Before
	void setupCommand() {
		command = injector.getInstance(ExecBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
		byteOutput = new ByteArrayOutputStream()
		command.setOutput(byteOutput)
	}

	static Injector injector

	static String konsoleCmd = "konsole --nofork --hide-menubar --hide-tabbar -e {}"

	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(
				new ExecModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
