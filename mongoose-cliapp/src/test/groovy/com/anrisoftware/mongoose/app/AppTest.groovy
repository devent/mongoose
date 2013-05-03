package com.anrisoftware.mongoose.app

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see App
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppTest {

	@Test
	void "scipt file [empty]"() {
		def file = folder.newFile("script.groovy");
		app.start(["-file", file] as String[])
	}

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	App app

	@Before
	void setupApp() {
		app = injector.getInstance(App)
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(new AppModule())
	}
}
