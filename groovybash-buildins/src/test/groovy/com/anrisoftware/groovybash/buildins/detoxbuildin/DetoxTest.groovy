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
package com.anrisoftware.groovybash.buildins.detoxbuildin

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.buildins.BuildinTestUtils

/**
 * Test the build-in command {@code detox}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class DetoxTest extends BuildinTestUtils {

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new DetoxModule()
	}

	@Test
	void "detox file trim white space characters"() {
		char b = 0x0B
		def result = createBuildin(DetoxBuildin, ["\t\n$b\f\rsomefilefoo\t\n$b\f\r"])()
		assert result == "somefilefoo"
	}

	@Test
	void "detox file with white space characters"() {
		char b = 0x0B
		def result = createBuildin(DetoxBuildin, ["some file\t\n$b\f\rfoo"])()
		assert result == "some_file_foo"
	}

	@Test
	void "detox file with punctuation bash characters"() {
		def result = createBuildin(DetoxBuildin, ["somefile\"'`foo"])()
		assert result == "somefile_foo"
	}
	
	@Test
	void "detox file with control characters"() {
		char f1 = 0x1F
		char f7 = 0x7F
		def result = createBuildin(DetoxBuildin, ["somefile\00\01\02$f1${f7}foo"])()
		assert result == "somefilefoo"
	}

	@Test
	void "detox file with punctuation sys characters"() {
		def result = createBuildin(DetoxBuildin, ["somefile/\\;:foo"])()
		assert result == "somefile_foo"
	}
	
	@Test
	void "return as file"() {
		def file = new File("somefilefoo")
		def result = createBuildin(DetoxBuildin, ["$file"])()
		File resultFile = result as File
		assert resultFile == file
	}
	
	@Test
	void "return as string"() {
		def file = new File("somefilefoo")
		def result = createBuildin(DetoxBuildin, ["${file.name}"])()
		def resultFile = result as String
		assert resultFile == file.absolutePath
	}
	
}
