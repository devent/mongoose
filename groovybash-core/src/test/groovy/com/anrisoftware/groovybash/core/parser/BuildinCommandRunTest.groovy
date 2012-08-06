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
 * Test the build-in command run.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class BuildinCommandRunTest extends CommandTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector()
	}

	@Test
	void "run build-in with cat command with file"() {
		def tmp = createTempFile "Hello World"
		def script = """
ret = run "cat $tmp"
echo ret
"""
		runParser script
		assertStringContent "Hello World0\n", output
	}

	@Test
	void "run build-in with custom environment passed as string"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
run "bash -x $tmp", "ENV_1=foo"
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "run build-in with custom environment passed as map variable"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
environment = [ENV_1: "foo"]
run "bash -x $tmp", environment
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "run build-in with custom environment passed as map argument"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
run "bash -x $tmp", [ENV_1: "foo"]
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "run build-in with custom working directory passed as string"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def script = """
run "bash $pwdScript", [:], "$tmp"
"""
		runParser script
		assertStringContent "$tmp\n", output
	}

	@Test
	void "run build-in with custom working directory passed as string using pwd"() {
		def tmp = createTempDirectory()
		def script = """
run 'bash -c "pwd"', [:], "$tmp"
"""
		runParser script
		assertStringContent "$tmp\n", output
	}

	@Test
	void "run build-in with custom working directory passed as file"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def script = """
workingDir = "$tmp" as File
run "bash $pwdScript", [:], workingDir
"""
		runParser script
		assertStringContent "$tmp\n", output
	}

	@Test
	void "parse cat command with file with newline"() {
		def tmp = createTempFile "Hello World\n"
		def script = """
cat "$tmp"
"""
		runParser script
		assertStringContent "Hello World\n", output
	}

	@Test
	void "parse cat command with file with no newline"() {
		def tmp = createTempFile "Hello World"
		def script = """
cat "$tmp"
"""
		runParser script
		assertStringContent "Hello World", output
	}

	@Test
	void "parse ls command"() {
		def script = """
ls "-lh"
"""
		runParser script
		log.info "output: ``{}''", output
	}

	@Test
	void "cat command with file output to file"() {
		def outputFile = createTempFile()
		def inputFile = createTempFile "Hello World"
		def script = """
cat out: "$outputFile", "$inputFile"
"""
		runParser script
		assertFileContent outputFile, "Hello World"
	}

	@Test
	void "cat command with output and error"() {
		def tmp = createTempFile "Hello World"
		def script = """
cat "nofile $tmp"
"""
		runParser script
		log.info "output: ``{}''", output
		log.info "error: ``{}''", error
	}

	@Test
	void "run command with custom environment as string"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
bash "$tmp", "ENV_1=foo"
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "run command with custom environment as map"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
bash "$tmp", [ENV_1: "foo"]
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "run command with custom working directory passed as string"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def script = """
bash "$pwdScript", [:], "$tmp"
"""
		runParser script
		assertStringContent "$tmp\n", output
	}

	@Test
	void "run command with custom working directory passed as string using pwd"() {
		def tmp = createTempDirectory()
		def script = """
bash "-c 'pwd'", [:], "$tmp"
"""
		runParser script
		assertStringContent "$tmp\n", output
	}

	@Test
	void "run command with custom working directory passed as file"() {
		def pwdScript = createTempFile 'pwd'
		def tmp = createTempDirectory()
		def script = """
workingDir = "$tmp" as File
bash "$pwdScript", [:], workingDir
"""
		runParser script
		assertStringContent "$tmp\n", output
	}
}
