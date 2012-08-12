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
package com.anrisoftware.groovybash.buildins.runbuildin

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.anrisoftware.groovybash.core.Buildin

/**
 * Test the build-in command {@code run}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class RunTest extends BuildinTestUtils {

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new RunModule()
	}

	@Test
	void "run build-in with cat command with file"() {
		def tmp = createTempFile "Hello World"
		createBuildin(RunBuildin, ["cat $tmp"])()
		assertStringContent output, "Hello World"
	}

	@Test
	void "run build-in return code from command"() {
		def result = createBuildin(RunBuildin, ['bash -c "exit 1"'])()
		assert result == 1
	}

	@Test
	void "run build-in with custom environment passed as string"() {
		def tmp = createTempFile 'echo $ENV_1'
		createBuildin(RunBuildin, ["bash $tmp", "ENV_1=foo"])()
		assertStringContent output, "foo\n"
	}

	@Test
	void "run build-in with custom environment passed as map"() {
		def tmp = createTempFile 'echo $ENV_1'
		def environment = [ENV_1: "foo"]
		createBuildin(RunBuildin, ["bash $tmp", environment])()
		assertStringContent output, "foo\n"
	}

	@Test
	void "run build-in with custom working directory passed as string"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def environment = [:]
		createBuildin(RunBuildin, ["bash $pwdScript", environment, "$tmp"])()
		assertStringContent output, "$tmp\n"
	}

	@Test
	void "run build-in with custom working directory passed as string using pwd"() {
		def tmp = createTempDirectory()
		def environment = [:]
		createBuildin(RunBuildin, ['bash -c "pwd"', environment, "$tmp"])()
		assertStringContent output, "$tmp\n"
	}

	@Test
	void "run build-in with custom working directory passed as file"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def environment = [:]
		createBuildin(RunBuildin, ["bash $pwdScript", environment, tmp])()
		assertStringContent output, "$tmp\n"
	}

	@Test
	void "run build-in redirect error stream to output stream"() {
		createBuildin(RunBuildin, [[redirectErrorStream: true], 'bash -xc "echo Text"'])()
		assertStringContent output, """+ echo Text
Text
"""
	}
	
	@Test
	void "run build-in output and error stream"() {
		createBuildin(RunBuildin, ['bash -xc "echo Text"'])()
		assertStringContent output, "Text\n"
		assertStringContent error, "+ echo Text\n"
	}
	
	@Test
	void "read command from input"() {
		inputBuffer = 'bash -c "echo Text"'.toString().bytes
		createBuildin(RunBuildin, [[in: inputStream, fromIn: true]])()
		assertStringContent output, "Text\n"
	}

}
