package com.anrisoftware.groovybash.core.parser

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
import com.anrisoftware.groovybash.core.factories.BashParserFactory
import com.anrisoftware.groovybash.core.plugins.PluginsModule
import com.google.inject.Injector

/**
 * Test the build-in command cd.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildinCdTest extends CommandTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector new ParserModule(), new EnvironmentModule(), new PluginsModule()
	}

	@Test
	void "parse cd buildin with no arguments"() {
		def script = """
cd
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assert parser.environment.workingDirectory == System.getProperty("user.home")as File
	}

	@Test
	void "parse cd buildin with directory"() {
		def tmp = createTempDirectory()
		def script = """
cd "$tmp"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assert parser.environment.workingDirectory == tmp
		tmp.deleteDir()
	}
}
