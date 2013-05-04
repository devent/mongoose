package com.anrisoftware.groovybash.parser

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.parser.ParserModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities to test the script parser.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParserTestUtils {

	static {
		TestUtils.toStringStyle
	}

	/**
	 * Binds {@link Environment} to the environment stub.
	 */
	static class EnvironmentStubModule extends AbstractModule {

		@Override
		protected void configure() {
			def environmentStub = new StubEnvironment()
			bind Environment toInstance environmentStub
		}
	}

	static Map createOutputStream() {
		def byteOutputStream = new ByteArrayOutputStream(1024)
		def outputStream = new PrintStream(byteOutputStream)
		System.setOut outputStream
		[byteOutputStream: byteOutputStream, outputStream: outputStream]
	}

	static Injector createInjector() {
		Guice.createInjector(
				new ParserModule(), new EnvironmentStubModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
