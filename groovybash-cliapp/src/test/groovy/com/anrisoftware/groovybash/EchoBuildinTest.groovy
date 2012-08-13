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
package com.anrisoftware.groovybash

import org.junit.Test

import com.google.inject.Injector

/**
 * Test the build-in command {@link EchoBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class EchoBuildinTest extends ScriptTestUtils {

	@Test
	void "parse echo buildin with no arguments"() {
		def script = """
echo
"""
		runParser script
		assertStringContent "\n", output
	}

	@Test
	void "parse echo buildin"() {
		def script = """
echo "Hello World", "Hello Again"
"""
		runParser script
		assertStringContent "Hello World Hello Again\n", output
	}

	@Test
	void "parse echo buildin with flag"() {
		def script = """
echo noNewline: true, "Hello World", "Hello Again"
"""
		print "``${output}''"
		runParser script
		assertStringContent "Hello World Hello Again", output
	}

	@Test
	void "parse echo buildin with flag no arguments"() {
		def script = """
echo noNewline: true
"""
		runParser script
		assertStringContent "", output
	}

	@Test
	void "parse echo buildin complex"() {
		def script = """
echo "Hello World"
echo
echo noNewline: true
echo noNewline: true, "Hello World", "Hello Again"
"""
		runParser script
		assertStringContent """Hello World

Hello World Hello Again""", output
	}

	@Test
	void "parse echo re-direct stdout to file"() {
		def tmp = createTempFile()
		tmp.delete()
		def script = """
file = "$tmp" as File
echo out: file, "Hello World"
"""
		runParser script
		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdout to file name"() {
		def tmp = createTempFile()
		tmp.delete()
		def script = """
file = "$tmp"
echo out: file, "Hello World"
"""
		runParser script
		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdout to stream"() {
		def tmp = createTempFile()
		tmp.delete()
		def script = """
stream = new PrintStream(new File("$tmp"))
echo out: stream, "Hello World"
"""
		runParser script
		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdin from file"() {
		def tmp = createTempFile "Hello World"
		def script = """
echo in: "$tmp"
"""
		runParser script
		assertStringContent "Hello World\n", output
	}

	@Test
	void "parse echo re-direct stdin from file no newline"() {
		def tmp = createTempFile "Hello World"
		def script = """
echo in: "$tmp", noNewline: true
"""
		runParser script
		assertStringContent "Hello World", output
	}

	@Test
	void "parse echo with number"() {
		def script = """
n = 0
echo n
"""
		runParser script
		assertStringContent "0\n", output
	}

	//@Test
	void "parse echo re-direct stdin from stream"() {
		def script = """
echo in: System.in
"""
		runParser script
		assertStringContent "1234\n\n", output
	}
}

