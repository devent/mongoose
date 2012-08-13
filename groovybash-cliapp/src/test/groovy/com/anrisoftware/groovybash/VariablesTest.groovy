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
package com.anrisoftware.groovybash

import groovy.util.logging.Slf4j

import org.junit.Test

import com.google.inject.Injector

/**
 * Test the build-in variables.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@Slf4j
class VariablesTest extends ScriptTestUtils {

	@Override
	Injector createInjector() {
		def injector = super.createInjector()
		injector.createChildInjector()
	}

	@Test
	void "echo command line arguments"() {
		def args = ["-a", "foo", "-b", "bar", "-c"]
		def script = """
echo ARGS
"""
		runParser script, args
		assertStringContent "[-a, foo, -b, bar, -c]\n", output
	}

	@Test
	void "echo pwd"() {
		def dir = createTempDirectory()
		def script = """
cd "$dir"
echo PWD
"""
		runParser script
		assertStringContent "$dir\n", output
	}
}
