package com.anrisoftware.groovybash.parser

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.core.Environment
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities to test the script parser.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class ParserTestUtils extends TestUtils {

	Injector injector

	BashParserFactory factory

	def environmentStub

	def byteOutputStream

	def outputStream

	/**
	 * Binds {@link Environment} to the environment stub.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 0.2
	 */
	static class EnvironmentStubModule extends AbstractModule {

		def environmentStub

		@Override
		protected void configure() {
			bind Environment toInstance environmentStub
		}
	}

	@Before
	void beforeTest() {
		injector = createInjector()
		factory = injector.getInstance BashParserFactory
		createOutputStream()
	}

	private createOutputStream() {
		byteOutputStream = new ByteArrayOutputStream(1024)
		outputStream = new PrintStream(byteOutputStream)
		System.setOut outputStream
	}

	Injector createInjector() {
		environmentStub = new StubEnvironment()
		Guice.createInjector new ParserModule(), new EnvironmentStubModule(environmentStub: environmentStub)
	}

	String getOutput() {
		byteOutputStream.toString()
	}
}
