package com.anrisoftware.groovybash.core.parser

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
import com.anrisoftware.groovybash.core.factories.BashParserFactory
import com.anrisoftware.groovybash.core.plugins.PluginsModule
import com.google.inject.Injector

/**
 * Test the build-in command {@link EchoBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildinEchoTest extends CommandTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector new ParserModule(), new EnvironmentModule(), new PluginsModule()
	}

	@Test
	void "parse echo buildin with no arguments"() {
		def script = """
echo
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "\n", output
	}

	@Test
	void "parse echo buildin"() {
		def script = """
echo "Hello World", "Hello Again"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World Hello Again\n", output
	}

	@Test
	void "parse echo buildin with flag"() {
		def script = """
echo nonewline: true, "Hello World", "Hello Again"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World Hello Again", output
	}

	@Test
	void "parse echo buildin with flag no arguments"() {
		def script = """
echo nonewline: true
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "", output
	}

	@Test
	void "parse echo buildin complex"() {
		def script = """
echo "Hello World"
echo
echo nonewline: true
echo nonewline: true, "Hello World", "Hello Again"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent """Hello World

Hello World Hello Again""", output
	}
}

