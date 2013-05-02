package com.anrisoftware.groovybash.parser

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.groovybash.parser.ParserTestUtils.*

import java.util.concurrent.Executors

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.exceptions.DirectoryNotFoundException
import com.anrisoftware.mongoose.parser.ScriptParserFactory
import com.google.inject.Injector

/**
 * Test the loading and running of Groovy script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParserTest {

	@Test
	void "run simple script"() {
		def script = """
println "Hello"
"""
		factory.create(script)()
		assertStringContent output(streams.byteOutputStream),
				"""Hello
"""
	}

	@Test
	void "check star imports"() {
		def script = """
println DirectoryNotFoundException.class
"""
		factory.create(script)()
		assertStringContent output(streams.byteOutputStream),
				"""${DirectoryNotFoundException.class}
"""
	}

	@Test
	void "check imports"() {
		def script = """
println Executors.class
"""
		factory.create(script)()
		assertStringContent output(streams.byteOutputStream),
				"""${Executors.class}
"""
	}

	Map streams

	@Before
	void setupStreams() {
		streams = createOutputStream()
	}

	static Injector injector

	static ScriptParserFactory factory

	@BeforeClass
	static void setupFactory() {
		injector = createInjector()
		factory = injector.getInstance ScriptParserFactory
	}
}
