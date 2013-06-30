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
package com.anrisoftware.mongoose.buildins.listfilesbuildin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.buildins.utils.BuildinsTestUtils.*

import org.apache.commons.io.FileUtils
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.environment.Environment
import com.google.inject.Injector


/**
 * @see ListFilesBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ListFilesTest {

	@Test
	void "list file name"() {
		environment.workingDirectory = directory
		def result = command("foo.txt").theFiles
		assert result.size() == 1
		assert result.contains(file("foo.txt"))
	}

	@Test
	void "list file name +2"() {
		environment.workingDirectory = directory
		def result = command("foo.txt", "bar.txt").theFiles
		assert result.size() == 2
		assert result.contains(file("foo.txt"))
		assert result.contains(file("bar.txt"))
	}

	@Test
	void "list file name +directory"() {
		def result = command("$directory/foo.txt").theFiles
		assert result.size() == 1
		assert result.contains(file("foo.txt"))
	}

	@Test
	void "no arguments"() {
		environment.workingDirectory = directory
		command.args()
		def result = command().theFiles
		assert result.size() == 9
	}

	@Test
	void "no arguments [+include sub]"() {
		environment.workingDirectory = directory
		def result = command(includeSubDirectories: true).theFiles
		assert result.size() == 10
		assert result.contains(file("sub_a"))
	}

	@Test
	void "no arguments [+recursive]"() {
		environment.workingDirectory = directory
		def result = command(recursive: true).theFiles
		assert result.size() == 15
		assert !result.contains(file("sub_a"))
		assert !result.contains(file("sub_a/sub_a"))
	}

	@Test
	void "no arguments [+recursive] [+include sub]"() {
		environment.workingDirectory = directory
		def result = command(recursive: true, includeSubDirectories: true).theFiles
		assert result.size() == 17
		assert result.contains(file("sub_a"))
		assert result.contains(file("sub_a/sub_a"))
	}

	@Test
	void "only txt [+recursive] [+include sub]"() {
		environment.workingDirectory = directory
		def result = command(recursive: true, includeSubDirectories: true, "*.txt").theFiles
		assert result.size() == 9
		assert result.contains(file("foo.txt"))
		assert result.contains(file("bar.txt"))
		assert result.contains(file("baz.txt"))
		assert result.contains(file("sub_a"))
		assert result.contains(file("sub_a/foo.txt"))
		assert result.contains(file("sub_a/bar.txt"))
		assert result.contains(file("sub_a/sub_a"))
		assert result.contains(file("sub_a/sub_a/foo.txt"))
		assert result.contains(file("sub_a/sub_a/bar.txt"))
	}

	@Test
	void "only txt [+1 recursive] [+include sub]"() {
		environment.workingDirectory = directory
		def result = command(recursive: true, depth: 1, includeSubDirectories: true, "*.txt").theFiles
		assert result.size() == 7
		assert result.contains(file("foo.txt"))
		assert result.contains(file("bar.txt"))
		assert result.contains(file("baz.txt"))
		assert result.contains(file("sub_a"))
		assert result.contains(file("sub_a/foo.txt"))
		assert result.contains(file("sub_a/bar.txt"))
		assert result.contains(file("sub_a/sub_a"))
	}

	@Test
	void "custom filter"() {
		environment.workingDirectory = directory
		def filter = { File pathname -> pathname.name.endsWith("jpg") } as FileFilter
		def result = command(filter: filter).theFiles
		assert result.size() == 3
		assert result.contains(file("foo.jpg"))
		assert result.contains(file("bar.jpg"))
		assert result.contains(file("baz.jpg"))
	}

	ListFilesBuildin command

	Environment environment

	static File directory

	static Injector injector

	@BeforeClass
	static void setupFiles() {
		directory = File.createTempDir()
		FileUtils.touch new File(directory, "foo.txt")
		FileUtils.touch new File(directory, "bar.txt")
		FileUtils.touch new File(directory, "baz.txt")
		FileUtils.touch new File(directory, "foo.jpg")
		FileUtils.touch new File(directory, "bar.jpg")
		FileUtils.touch new File(directory, "baz.jpg")
		FileUtils.touch new File(directory, "foo.x")
		FileUtils.touch new File(directory, "bar.x")
		FileUtils.touch new File(directory, "baz.x")
		def sub = new File(directory, "sub_a")
		sub.mkdir()
		FileUtils.touch new File(sub, "foo.txt")
		FileUtils.touch new File(sub, "bar.txt")
		FileUtils.touch new File(sub, "baz.jpg")
		def subsub = new File(directory, "sub_a/sub_a")
		sub.mkdir()
		FileUtils.touch new File(subsub, "foo.txt")
		FileUtils.touch new File(subsub, "bar.txt")
		FileUtils.touch new File(subsub, "baz.jpg")
	}

	@AfterClass
	static void removeFiles() {
		FileUtils.deleteDirectory directory
	}

	static file(String name) {
		new File(directory, name)
	}

	@Before
	void setupCommand() {
		environment = createEnvironment injector
		command = createCommand injector, environment
	}

	@BeforeClass
	static void setupInjector() {
		injector = createInjector().createChildInjector(new ListFilesModule())
	}
}
