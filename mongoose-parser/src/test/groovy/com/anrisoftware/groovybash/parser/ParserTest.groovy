package com.anrisoftware.groovybash.parser

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.groovybash.parser.ParserTestUtils.*

import java.util.concurrent.Executors

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.environment.Environment
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
		def script = createReader("""
println "Hello"
""")
		def parser = factory.create(script, "Test")
		parser.setEnvironment environment
		parser()
		assertStringContent output(streams.byteOutputStream),
				"""Hello
"""
	}

	@Test
	void "check star imports"() {
		def script = createReader("""
println DirectoryNotFoundException.class
""")
		def parser = factory.create(script, "Test")
		parser.setEnvironment environment
		parser()
		assertStringContent output(streams.byteOutputStream),
				"""${DirectoryNotFoundException.class}
"""
	}

	@Test
	void "check imports"() {
		def script = createReader("""
println Executors.class
""")
		def parser = factory.create(script, "Test")
		parser.setEnvironment environment
		parser()
		assertStringContent output(streams.byteOutputStream),
				"""${Executors.class}
"""
	}

	Map streams

	Environment environment

	@Before
	void setupStreams() {
		streams = createOutputStream()
		environment = new StubEnvironment()
	}

	static Injector injector

	static ScriptParserFactory factory

	@BeforeClass
	static void setupFactory() {
		injector = createInjector()
		factory = injector.getInstance ScriptParserFactory
	}

	static Reader createReader(String text) {
		new StringReader(text)
	}
}
