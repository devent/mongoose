/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.core.parser

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
import com.anrisoftware.groovybash.core.executor.ExecutorModule
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
		injector.createChildInjector new ParserModule(),
						new EnvironmentModule(), new PluginsModule(),
						new ExecutorModule()
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

	@Test
	void "parse echo re-direct stdout to file"() {
		def tmp = createTempFile()
		tmp.delete()

		def script = """
file = "$tmp" as File
echo out: file, "Hello World"
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdout to file name"() {
		def tmp = createTempFile()
		tmp.delete()

		def script = """
file = "$tmp"
echo out: file, "Hello World"
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdout to stream"() {
		def tmp = createTempFile()
		tmp.delete()

		def script = """
stream = new PrintStream(new File("$tmp"))
echo out: stream, "Hello World"
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertFileContent tmp, "Hello World\n"
	}

	@Test
	void "parse echo re-direct stdin from file"() {
		def tmp = createTempFile "Hello World"

		def script = """
echo in: "$tmp"
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World\n", output
	}

	@Test
	void "parse echo re-direct stdin from file no newline"() {
		def tmp = createTempFile "Hello World"

		def script = """
echo in: "$tmp", nonewline: true
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World", output
	}

	@Test
	void "parse echo with number"() {
		def script = """
n = 0
echo n
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "0\n", output
	}

	//@Test
	void "parse echo re-direct stdin from stream"() {
		def script = """
echo in: System.in
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "1234\n\n", output
	}
}

