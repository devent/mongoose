package com.anrisoftware.groovybash.parser

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.junit.Test
import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.Option

import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFoundException

/**
 * Test the loading and running of Groovy script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class ParserTest extends ParserTestUtils {

	@Test
	void "run simple script"() {
		def script = """
println "Hello"
"""
		factory.create(script).run()
		assertStringContent output, "Hello\n"
	}

	@Test
	void "check star imports"() {
		def script = """
println DirectoryNotFoundException.class
"""
		factory.create(script).run()
		assertStringContent output, "${DirectoryNotFoundException.class}\n"
	}

	@Test
	void "check imports"() {
		def script = """
println Argument.class
println Option.class
println FilenameUtils.class
println FileUtils.class
"""
		factory.create(script).run()
		assertStringContent output, """${Argument.class}
${Option.class}
${FilenameUtils.class}
${FileUtils.class}
"""
	}
}
