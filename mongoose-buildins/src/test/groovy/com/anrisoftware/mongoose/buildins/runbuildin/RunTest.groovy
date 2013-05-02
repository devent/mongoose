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
package com.anrisoftware.mongoose.buildins.runbuildin
import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.commans.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see RunBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RunTest {

	@Test
	void "cat file"() {
		def string = "Test"
		def tmp = File.createTempFile("file", "")
		try {
			write tmp, string
			command "cat $tmp"
			assertStringContent output(byteOutput), string
		} finally {
			tmp.delete()
		}
	}

	@Test
	void "cat file [+ working directory]"() {
		def string = "Test"
		def dir = File.createTempDir()
		def tmp = new File(dir, "file")
		try {
			write tmp, string
			command "cat ${tmp.name}", [:], dir
			assertStringContent output(byteOutput), string
		} finally {
			dir.deleteDir()
		}
	}

	@Test
	void "bash [+env variable]"() {
		def scriptFile = File.createTempFile("bash", null)
		def string = "Test"
		try {
			write scriptFile, 'echo -n $VAR'
			command "bash $scriptFile", [VAR: string]
			assertStringContent output(byteOutput), string
		} finally {
			scriptFile.delete()
		}
	}

	@Test(expected = CommandException)
	void "sleep [+timeout]"() {
		command timeout: 1000, "sleep 2"
	}

	@Test
	void "exit value"() {
		def value = 99
		def scriptFile = File.createTempFile("bash", null)
		try {
			write scriptFile, "exit $value"
			command successExitValue: value, "bash $scriptFile"
		} finally {
			scriptFile.delete()
		}
	}

	@Test
	void "exit values"() {
		def value = 99
		def scriptFile = File.createTempFile("bash", null)
		try {
			write scriptFile, "exit $value"
			command successExitValues: [value], "bash $scriptFile"
		} finally {
			scriptFile.delete()
		}
	}

	RunBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	@Before
	void setupCommand() {
		command = injector.getInstance(RunBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
		byteOutput = new ByteArrayOutputStream()
		command.setOutput(byteOutput)
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(
				new RunModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
