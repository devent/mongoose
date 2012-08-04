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
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFoundException
import com.anrisoftware.groovybash.core.executor.ExecutorModule
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
		injector.createChildInjector new ParserModule(),
						new EnvironmentModule(), new PluginsModule(),
						new ExecutorModule()
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
	void "parse cd buildin with file"() {
		def tmp = createTempDirectory()
		def script = """
dir = new File("$tmp")
cd dir
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
		shouldFailWith DirectoryNotFoundException, { parser.run() }

		assert parser.environment.workingDirectory == new File(".")
	}

	@Test
	void "parse cd buildin with not existent directory catch exception"() {
		def tmp = createTempDirectory()
		assert tmp.deleteDir()

		def script = """
try {
	cd "$tmp"
} catch (DirectoryNotFoundException e) {
    echo e
}
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assert parser.environment.workingDirectory == new File(".")
		assert output =~ /^com\.anrisoftware\.groovybash\.core\.exceptions\.DirectoryNotFoundException: The directory \/tmp\/\d+-\d+ could not be found\./
	}
}
