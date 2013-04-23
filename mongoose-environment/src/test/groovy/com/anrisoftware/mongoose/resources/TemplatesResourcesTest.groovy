package com.anrisoftware.mongoose.resources

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see TemplatesResources
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TemplatesResourcesTest {

	@Test
	void "access text"() {
		def text = "Test"
		assert resources.TemplateName.the_resource("text", text) == "$text"
		resources.setLocale Locale.GERMAN
		assert resources.TemplateName.the_resource("text", text) == "$text German"
	}

	TemplatesResources resources

	@Before
	void createResources() {
		resources = injector.getInstance(TemplatesResources)
		resources.setClassLoader(TemplatesResourcesTest.class.classLoader)
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		injector = Guice.createInjector(new ResourcesModule())
	}
}
