package com.anrisoftware.groovybash.parser

import org.junit.Before

import com.anrisoftware.groovybash.core.Environment
import com.anrisoftware.groovybash.core.factories.BashParserFactory
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

class ParserTestUtils {

	Injector injector

	BashParserFactory factory

	def environmentStub

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
	}

	Injector createInjector() {
		environmentStub = new StubEnvironment()
		Guice.createInjector new ParserModule(), new EnvironmentStubModule(environmentStub: environmentStub)
	}
}
