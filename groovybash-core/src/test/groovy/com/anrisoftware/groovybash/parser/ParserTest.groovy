package com.anrisoftware.groovybash.parser

import org.junit.Test

class ParserTest extends ParserTestUtils {

	@Test
	void "run simple script"() {
		def script = """
println "Hello"
"""
		factory.create(script).run()
	}
}
