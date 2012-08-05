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

import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
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
@Slf4j
class ParseCommandCatTest extends CommandTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector new ParserModule(),
						new EnvironmentModule(), new PluginsModule(),
						new ExecutorModule()
	}

	@Test
	void "run build-in with cat command with file"() {
		def tmp = createTempFile "Hello World"
		def script = """
ret = run "cat $tmp"
echo ret
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World0\n", output
	}

	@Test
	void "run build-in with custom environment passed as string"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
run "bash -x $tmp", "ENV_1=foo"
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "foo\n", output
	}

	@Test
	void "run build-in with custom environment passed as map variable"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
environment = [ENV_1: "foo"]
run "bash -x $tmp", environment
"""
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "foo\n", output
	}

	@Test
	void "run build-in with custom environment passed as map argument"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
run "bash -x $tmp", [ENV_1: "foo"]
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "parse cat command with file with newline"() {
		def tmp = createTempFile "Hello World\n"
		def script = """
cat "$tmp"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World\n", output
	}

	@Test
	void "parse cat command with file with no newline"() {
		def tmp = createTempFile "Hello World"
		def script = """
cat "$tmp"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertStringContent "Hello World", output
	}

	@Test
	void "parse ls command"() {
		def script = """
ls "-lh"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		log.info "output: ``{}''", output
	}

	@Test
	void "cat command with file output to file"() {
		def outputFile = createTempFile()
		def inputFile = createTempFile "Hello World"
		def script = """
cat out: "$outputFile", "$inputFile"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		assertFileContent outputFile, "Hello World"
	}

	@Test
	void "cat command with output and error"() {
		def tmp = createTempFile "Hello World"
		def script = """
cat "nofile $tmp"
"""

		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.run()

		log.info "output: ``{}''", output
		log.info "error: ``{}''", error
	}

	@Test
	void "echo command with custom environment as string"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
bash "$tmp", "ENV_1=foo"
"""
		runParser script
		assertStringContent "foo\n", output
	}

	@Test
	void "echo command with custom environment as map"() {
		def tmp = createTempFile 'echo $ENV_1'
		def script = """
bash "$tmp", [ENV_1: "foo"]
"""
		runParser script
		assertStringContent "foo\n", output
	}
}
