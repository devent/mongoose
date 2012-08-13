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
package com.anrisoftware.groovybash.buildins.listfilesbuildin

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.google.common.io.Files

/**
 * Test the build-in command {@code listFiles}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ListFilesTest extends BuildinTestUtils {

	static File directory

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

	static file(def name) {
		new File(directory, name)
	}

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new ListFilesModule()
	}

	@Test
	void "list file name"() {
		environment.workingDirectory = directory
		def result = createBuildin(ListFilesBuildin, ["foo.txt"])()
		assert result.files == [file("foo.txt")]
		assertStringContent result.toString(), "[$directory/foo.txt]"
	}

	@Test
	void "listFiles buildin with file names from list"() {
		environment.workingDirectory = directory
		def list = ["foo.txt", "bar.txt"]
		def result = createBuildin(ListFilesBuildin, [list])()
		assert result.files == [file("foo.txt"), file("bar.txt")]
		assertStringContent result.toString(), "[$directory/foo.txt, $directory/bar.txt]"
	}
	
	@Test
	void "listFiles buildin with no arguments"() {
		environment.workingDirectory = directory
		def result = createBuildin(ListFilesBuildin)()
		assert result.files == [file("baz.x"), file("bar.x"), file("foo.x"), file("baz.jpg"), file("bar.jpg"), file("foo.jpg"), file("baz.txt"), file("bar.txt"), file("foo.txt")]
		assertStringContent result.toString(), "[$directory/baz.x, $directory/bar.x, $directory/foo.x, $directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]"
	}

	@Test
	void "listFiles buildin with wildcat"() {
		environment.workingDirectory = directory
		def result = createBuildin(ListFilesBuildin, ["*.txt"])()
		assert result.files == [file("baz.txt"), file("bar.txt"), file("foo.txt")]
		assertStringContent result.toString(), "[$directory/baz.txt, $directory/bar.txt, $directory/foo.txt]"
	}

	@Test
	void "listFiles buildin with wildcat and test against a list"() {
		environment.workingDirectory = directory
		def result = createBuildin(ListFilesBuildin, ["*.txt"])()
		assert result == [file("baz.txt"), file("bar.txt"), file("foo.txt")]
	}

	@Test
	void "listFiles buildin with multiple wildcats"() {
		environment.workingDirectory = directory
		def result = createBuildin(ListFilesBuildin, ["*.jpg", "*.txt"])()
		assert result.files == [file("baz.jpg"), file("bar.jpg"), file("foo.jpg"), file("baz.txt"), file("bar.txt"), file("foo.txt")]
		assertStringContent result.toString(), "[$directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]"
	}

	@Test
	void "listFiles buildin with multiple wildcats from list"() {
		environment.workingDirectory = directory
		def list = ["*.jpg", "*.txt"]
		def result = createBuildin(ListFilesBuildin, [list])()
		assert result.files == [file("baz.jpg"), file("bar.jpg"), file("foo.jpg"), file("baz.txt"), file("bar.txt"), file("foo.txt")]
		assertStringContent result.toString(), "[$directory/baz.jpg, $directory/bar.jpg, $directory/foo.jpg, $directory/baz.txt, $directory/bar.txt, $directory/foo.txt]"
	}

}
