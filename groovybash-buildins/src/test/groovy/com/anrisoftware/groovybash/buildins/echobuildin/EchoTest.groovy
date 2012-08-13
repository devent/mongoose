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
package com.anrisoftware.groovybash.buildins.echobuildin

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.anrisoftware.groovybash.core.Buildin

/**
 * Test the build-in command {@code echo}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class EchoTest extends BuildinTestUtils {

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new EchoModule()
	}

	@Test
	void "echo no text"() {
		createBuildin(EchoBuildin)()
		assertStringContent output, "\n"
	}

	@Test
	void "echo with text"() {
		def text = "Text"
		createBuildin(EchoBuildin, [text])()
		assertStringContent output, "$text\n"
	}

	@Test
	void "echo with multiple texts"() {
		def textA = "TextA"
		def textB = "TextB"
		createBuildin(EchoBuildin, [textA, textB])()
		assertStringContent output, "$textA $textB\n"
	}

	@Test
	void "echo no text no noewline"() {
		createBuildin(EchoBuildin, [[noNewline: true]])()
		assertStringContent output, ""
	}

	@Test
	void "echo with text no newline"() {
		def text = "Text"
		createBuildin(EchoBuildin, [[noNewline: true], text])()
		assertStringContent output, "$text"
	}

	@Test
	void "echo from input"() {
		def text = "Text"
		inputBuffer = text.bytes
		createBuildin(EchoBuildin, [[in: inputStream]])()
		assertStringContent output, "$text\n"
	}

	@Test
	void "echo from input no newline"() {
		def text = "Text"
		inputBuffer = text.bytes
		createBuildin(EchoBuildin, [[in: inputStream, noNewline: true]])()
		assertStringContent output, "$text"
	}

	@Test
	void "echo line read from input"() {
		inputBuffer = "TextA\nTextB".bytes
		createBuildin(EchoBuildin, [[in: inputStream, fromIn: true]])()
		assertStringContent output, "TextA\n"
	}

}
