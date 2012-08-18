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

import org.junit.Before
import org.junit.Test

import com.google.common.io.Files

/**
 * Test text resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
@Slf4j
class TextResourcesTest extends ScriptTestUtils {

	static packageName = "com.anrisoftware.test"

	static textFoo = """Foo Text
"""
	static textFoo_de = """Foo Text Deutsch
"""
	static texts = """text_foo = text_foo.txt
"""
	static texts_de = """text_foo = text_foo_de.txt
"""

	@Before
	void beforeTest() {
		super.beforeTest()
		def packageDir = createPackageDir()
		Files.write texts, new File(packageDir, "Texts.properties"), charset
		Files.write texts_de, new File(packageDir, "Texts_de.properties"), charset
		Files.write textFoo, new File(packageDir, "text_foo.txt"), charset
		Files.write textFoo_de, new File(packageDir, "text_foo_de.txt"), charset
		System.setProperty "com.anrisoftware.groovybash.parser.script_home", tmpdir
	}

	File createPackageDir() {
		File tmpdir = new File(tmpdir)
		File packageDir = new File(tmpdir, packageName.replaceAll("\\.", "/"))
		packageDir.mkdirs()
		return packageDir
	}

	String getTmpdir() {
		System.getProperty("java.io.tmpdir")
	}

	@Test
	void "different locales"() {
		def script = """
package $packageName

Locale.defaultLocale = Locale.ENGLISH
echo TEXTS.Texts.text_foo

Locale.defaultLocale = Locale.GERMAN
echo TEXTS.Texts.text_foo
"""
		def parser = runParser script
		assertStringContent output, "$textFoo\n$textFoo_de\n"
	}
}

