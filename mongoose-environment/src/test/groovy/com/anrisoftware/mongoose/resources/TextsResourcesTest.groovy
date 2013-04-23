package com.anrisoftware.mongoose.resources

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see TextsResources
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TextsResourcesTest {

	@Test
	void "access text"() {
		assert resources.TextsName.the_resource == "Test"
		resources.setLocale Locale.GERMAN
		assert resources.TextsName.the_resource == "Test German"
	}

	TextsResources resources

	@Before
	void createResources() {
		resources = injector.getInstance(TextsResources)
		resources.setClassLoader(TextsResourcesTest.class.classLoader)
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		injector = Guice.createInjector(new ResourcesModule())
	}
}
