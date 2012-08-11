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

}
