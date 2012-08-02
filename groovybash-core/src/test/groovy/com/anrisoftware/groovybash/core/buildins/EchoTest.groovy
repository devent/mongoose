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
package com.anrisoftware.groovybash.core.buildins

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.buildins.echobuildin.EchoBuildin;

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

