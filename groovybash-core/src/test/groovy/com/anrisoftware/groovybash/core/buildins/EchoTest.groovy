package com.anrisoftware.groovybash.core.buildins

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils

/**
 * Test the build-in command {@link EchoBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoTest extends CommandTestUtils {

	@Test
	void "echo no arguments"() {
		EchoBuildin buildin = injector.getInstance EchoBuildin
		buildin.call()
		assertStringContent output, "\n"
	}

	@Test
	void "echo with argument"() {
		String var = "Hello World"
		EchoBuildin buildin = injector.getInstance EchoBuildin
		buildin.setArguments "$var"
		buildin.call()
		assertStringContent output, "$var\n"
	}

	@Test
	void "echo with multiple arguments"() {
		String[] var = ["Hello World A", "Hello World B"]
		EchoBuildin buildin = injector.getInstance EchoBuildin
		buildin.setArguments var[0], var[1]
		buildin.call()
		assertStringContent output, "${var[0]} ${var[1]}\n"
	}

	@Test
	void "echo no arguments no newline"() {
		EchoBuildin buildin = injector.getInstance EchoBuildin
		buildin.nonewline().call()
		assertStringContent output, ""
	}

	@Test
	void "echo with arguments no newline"() {
		String var = "Hello World"
		EchoBuildin buildin = injector.getInstance EchoBuildin
		buildin.setArguments "$var"
		buildin.nonewline().call()
		assertStringContent output, "$var"
	}
}

