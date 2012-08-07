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

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.google.inject.Injector

/**
 * Test the build-in command cd.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildinLogTest extends CommandTestUtils {

	static scriptTestLogFile = "script_test.log" as File

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector()
	}

	@Test
	void "parse debug buildin with message"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging"
"""
		def parser = runParser script
		assertFileContent scriptTestLogFile, "DEBUG: Debug logging\n"
	}

	@Test
	void "parse debug buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging {}", "foo"
"""
		def parser = runParser script
		assertFileContent scriptTestLogFile, "DEBUG: Debug logging foo\n"
	}

	@Test
	void "parse debug buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging {} {}", "foo", 10
"""
		def parser = runParser script
		assertFileContent scriptTestLogFile, "DEBUG: Debug logging foo 10\n"
	}
}
