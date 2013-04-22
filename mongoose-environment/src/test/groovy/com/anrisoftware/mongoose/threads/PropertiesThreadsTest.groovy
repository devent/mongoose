package com.anrisoftware.mongoose.threads;

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.propertiesutils.ContextPropertiesFactory
import com.google.inject.Guice
import com.google.inject.Injector

class PropertiesThreadsTest {

	PropertiesThreads threads

	@Test
	void "cached pool"() {
		threads.setProperties properties
		threads.setName "cached"
	}

	@Before
	void createThreads() {
		threads = injector.getInstance PropertiesThreads
	}

	static Injector injector

	static URL propertiesResource

	static Properties properties

	@BeforeClass
	static void setupThreads() {
		injector = Guice.createInjector(new ThreadsModule())
		propertiesResource = PropertiesThreadsTest.class.getResource("threads_test.properties")
		properties = new ContextPropertiesFactory(PropertiesThreads).fromResource(propertiesResource)
	}
}
