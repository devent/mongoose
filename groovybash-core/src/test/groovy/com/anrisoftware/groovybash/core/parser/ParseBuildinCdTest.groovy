package com.anrisoftware.groovybash.core.parser

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFound
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
	void "parse cd buildin with no arguments return value"() {
		def script = """
ret = cd
echo ret
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assert parser.environment.workingDirectory == System.getProperty("user.home")as File
		assertStringContent "0\n", output
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

	@Test
	void "parse cd buildin with not existent directory"() {
		def tmp = createTempDirectory()
		assert tmp.deleteDir()

		def script = """
cd "$tmp"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		shouldFailWith DirectoryNotFound, { parser.run() }

		assert parser.environment.workingDirectory == new File(".")
	}

	@Test
	void "parse cd buildin with not existent directory catch exception"() {
		def tmp = createTempDirectory()
		assert tmp.deleteDir()

		def script = """
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFound
try {
	cd "$tmp"
} catch (DirectoryNotFound e) {
    echo e
}
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assert parser.environment.workingDirectory == new File(".")
		assert output =~ /^com\.anrisoftware\.groovybash\.core\.exceptions\.DirectoryNotFound: The directory \/tmp\/\d+-\d+ could not be found\./
	}
}
