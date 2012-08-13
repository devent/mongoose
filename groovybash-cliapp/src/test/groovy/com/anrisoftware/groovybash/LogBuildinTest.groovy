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
package com.anrisoftware.groovybash

import org.junit.Test

/**
 * Test the build-in command cd.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class LogBuildinTest extends ScriptTestUtils {

	static File scriptTestLogFile = "script_test.log" as File

	/**
	 * Accumulate the expected result logging messages, like in the file.
	 */
	static StringBuilder accumulate = new StringBuilder()

	@Test
	void "parse info buildin with message"() {
		def script = """
package com.anrisoftware.test.script
info "Info logging"
"""
		def parser = runParser script
		accumulate << "INFO: Info logging\n"
		////assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse info buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
info "Info logging {}", "foo"
"""
		def parser = runParser script
		accumulate << "INFO: Info logging foo\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse info buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
info "Info logging {} {}", "foo", 10
"""
		def parser = runParser script
		accumulate << "INFO: Info logging foo 10\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse debug buildin with message"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging"
"""
		def parser = runParser script
		accumulate << "DEBUG: Debug logging\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse debug buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging {}", "foo"
"""
		def parser = runParser script
		accumulate << "DEBUG: Debug logging foo\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse debug buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
debug "Debug logging {} {}", "foo", 10
"""
		def parser = runParser script
		accumulate << "DEBUG: Debug logging foo 10\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse warn buildin with message"() {
		def script = """
package com.anrisoftware.test.script
warn "Warn logging"
"""
		def parser = runParser script
		accumulate << "WARN: Warn logging\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse warn buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
warn "Warn logging {}", "foo"
"""
		def parser = runParser script
		accumulate << "WARN: Warn logging foo\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse warn buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
warn "Warn logging {} {}", "foo", 10
"""
		def parser = runParser script
		accumulate << "WARN: Warn logging foo 10\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse trace buildin with message"() {
		def script = """
package com.anrisoftware.test.script
trace "Trace logging"
"""
		def parser = runParser script
		accumulate << "TRACE: Trace logging\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse trace buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
trace "Trace logging {}", "foo"
"""
		def parser = runParser script
		accumulate << "TRACE: Trace logging foo\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse trace buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
trace "Trace logging {} {}", "foo", 10
"""
		def parser = runParser script
		accumulate << "TRACE: Trace logging foo 10\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse error buildin with message"() {
		def script = """
package com.anrisoftware.test.script
error "Error logging"
"""
		def parser = runParser script
		accumulate << "ERROR: Error logging\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse error buildin with message and argument"() {
		def script = """
package com.anrisoftware.test.script
error "Error logging {}", "foo"
"""
		def parser = runParser script
		accumulate << "ERROR: Error logging foo\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse error buildin with message and arguments"() {
		def script = """
package com.anrisoftware.test.script
error "Error logging {} {}", "foo", 10
"""
		def parser = runParser script
		accumulate << "ERROR: Error logging foo 10\n"
		//assertFileContent scriptTestLogFile, accumulate.toString()
	}

	@Test
	void "parse error buildin with message and exception"() {
		def script = """
package com.anrisoftware.test.script
error "Error logging", new Exception("foo")
"""
		def parser = runParser script
	}
}
