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

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.core.CommandTestUtils
import com.google.common.io.Files
import com.google.inject.Injector

/**
 * Test the build-in command {@code listFiles}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class BuildinListFilesTest extends CommandTestUtils {

	static File directory

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector()
	}

	@BeforeClass
	static void beforeClass() {
		directory = new TestUtils().createTempDirectory { d ->
			Files.touch new File(d, "foo.txt")
			Files.touch new File(d, "bar.txt")
			Files.touch new File(d, "baz.txt")
			Files.touch new File(d, "foo.jpg")
			Files.touch new File(d, "bar.jpg")
			Files.touch new File(d, "baz.jpg")
			Files.touch new File(d, "foo.x")
			Files.touch new File(d, "bar.x")
			Files.touch new File(d, "baz.x")
		}
	}

	@Test
	void "listFiles buildin with file name"() {
		def script = """
cd "$directory"
files = listFiles "foo.txt"
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/foo.txt]\n", output
	}

	@Test
	void "listFiles buildin with file names from list"() {
		def script = """
cd "$directory"
list = ["foo.txt", "bar.txt"]
files = listFiles list
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/foo.txt, $directory/bar.txt]\n", output
	}

	@Test
	void "listFiles buildin with no arguments"() {
		def script = """
cd "$directory"
files = listFiles
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/baz.x, $directory/bar.x, $directory/foo.x, $directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]\n", output
	}

	@Test
	void "listFiles buildin with wildcat"() {
		def script = """
cd "$directory"
files = listFiles "*.txt"
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/baz.txt, $directory/bar.txt, $directory/foo.txt]\n", output
	}

	@Test
	void "listFiles buildin with multiple wildcats"() {
		def script = """
cd "$directory"
files = listFiles "*.jpg", "*.txt"
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]\n", output
	}

	@Test
	void "listFiles buildin with multiple wildcats from list"() {
		def script = """
cd "$directory"
list = ["*.jpg", "*.txt"]
files = listFiles list
echo files
"""
		def parser = runParser script
		assertStringContent "[$directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]\n", output
	}
}
