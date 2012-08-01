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
package com.anrisoftware.groovybash.parameter

import static com.anrisoftware.groovybash.core.plugins.PluginsModule.*

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Setups the standard streams to test commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandTestUtils extends TestUtils {
	
	def pluginsProperties = resourceURLFromObject "plugins.properties"

	def inputBuffer

	def inputStream

	def outputStream

	def errorStream
	
	def byteOutputStream

	def byteErrorStream
	
	Injector injector

	def modules

	@Before
	void beforeTest() {
		setupCacheProperties()
		inputBuffer = []as byte[]
		inputStream = new ByteArrayInputStream(inputBuffer)
		byteOutputStream = new ByteArrayOutputStream(1024)
		outputStream = new PrintStream(byteOutputStream)
		byteErrorStream = new ByteArrayOutputStream(1024)
		errorStream = new PrintStream(byteErrorStream)
		injector = createInjector()
	}
	
	def setupCacheProperties() {
		System.setProperty(PLUGINS_PROPERTIES_RESOURCE, pluginsProperties.toString())
	}
	
	Injector createInjector() {
		Guice.createInjector modules
	}
	
	void setInputBuffer(byte[] inputBuffer) {
		this.inputBuffer = inputBuffer
		inputStream = new ByteArrayInputStream(inputBuffer)
	}

	String getOutput() {
		byteOutputStream.toString()
	}

}
