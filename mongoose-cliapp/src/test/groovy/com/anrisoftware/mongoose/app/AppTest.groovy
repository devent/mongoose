package com.anrisoftware.mongoose.app

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

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

	@Test
	void "scipt file [echo]"() {
		def file = folder.newFile("script.groovy");
		write file, """echo \"Hello\""""
		app.start(["-file", file] as String[])
	}

	@Test
	void "scipt file [map args]"() {
		def file = folder.newFile("script.groovy");
		write file, """echo newline: false, \"Hello\""""
		app.start(["-file", file] as String[])
	}

	@Test
	void "scipt file [external]"() {
		def txt = folder.newFile("file.txt");
		write txt, """Hello"""
		def file = folder.newFile("script.groovy");
		write file, """cat \"$txt\""""
		app.start(["-file", file] as String[])
	}

	@Test
	void "scipt file [external, map]"() {
		def txt = folder.newFile("file.txt");
		write txt, """Hello"""
		def file = folder.newFile("script.groovy");
		write file, """cat timeout: 500, \"$txt\""""
		app.start(["-file", file] as String[])
	}

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	Injector injector

	App app

	@Before
	void setupApp() {
		injector = Guice.createInjector(new AppModule())
		app = injector.getInstance(App)
	}

	@BeforeClass
	static void setupStringStyle() {
		toStringStyle
	}
}
