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
package com.anrisoftware.groovybash.buildins.cdbuildin

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.anrisoftware.groovybash.core.Buildin

/**
 * Test the build-in command {@code cd}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CdTest extends BuildinTestUtils {

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new CdModule()
	}

	@Test
	void "cd to user home"() {
		createBuildin(CdBuildin)()
		assert environment.workingDirectory == environment.userHome
	}

	@Test
	void "cd to directory name"() {
		def dir = createTempDirectory()
		createBuildin(CdBuildin, ["$dir"])()
		assert environment.workingDirectory == dir
	}

	@Test
	void "cd to directory file"() {
		def dir = createTempDirectory()
		createBuildin(CdBuildin, [dir])()
		assert environment.workingDirectory == dir
	}
	
	@Test
	void "cd to directory read from input"() {
		def dir = createTempDirectory()
		inputBuffer = "$dir".toString().bytes
		createBuildin(CdBuildin, [[in: inputStream, fromIn: true]])()
		assert environment.workingDirectory == dir
	}

	@Test
	void "cd to directory read from input terminated with newline"() {
		def dir = createTempDirectory()
		inputBuffer = "$dir\n".toString().bytes
		createBuildin(CdBuildin, [[in: inputStream, fromIn: true]])()
		assert environment.workingDirectory == dir
	}
}
