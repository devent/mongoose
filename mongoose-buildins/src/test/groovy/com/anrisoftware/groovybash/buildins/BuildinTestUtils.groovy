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
package com.anrisoftware.groovybash.buildins

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.core.Buildin
import com.anrisoftware.groovybash.core.Environment
import com.anrisoftware.groovybash.environment.EnvironmentModule
import com.anrisoftware.groovybash.executor.ExecutorModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Setups the standard streams to test the build-in commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class BuildinTestUtils extends TestUtils {

	def inputBuffer

	def inputStream

	def outputStream

	def errorStream
	
	def byteOutputStream

	def byteErrorStream
	
	Injector injector
	
	Environment environment

	/**
	 * Sets the standard streams of the build-in commands.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 0.1
	 */
	static class BuildinsTestModule extends BuildinsModule {

		def inputStream

		def outputStream

		def errorStream
	
		@Override
		StandardStreams getStandardStreams() {
			new StandardStreams(inputStream, outputStream, errorStream)
		}
	}
	
	@Before
	void beforeTest() {
		createStandardStreams()
		injector = createInjector()
		environment = injector.getInstance Environment
	}

	private createStandardStreams() {
		inputBuffer = []as byte[]
		inputStream = new ByteArrayInputStream(inputBuffer)
		byteOutputStream = new ByteArrayOutputStream(1024)
		outputStream = new PrintStream(byteOutputStream)
		byteErrorStream = new ByteArrayOutputStream(1024)
		errorStream = new PrintStream(byteErrorStream)
	}
	
	Injector createInjector() {
		Guice.createInjector new BuildinsTestModule(
			inputStream: inputStream, outputStream: outputStream, errorStream: errorStream),
		new EnvironmentModule(), new ExecutorModule()
	}
	
	Buildin createBuildin(Class buildinClass, def args = null) {
		Buildin buildin = injector.getInstance buildinClass
		buildin.environment = environment
		if (args != null) {
			buildin.arguments = args
		}
		return buildin
	}
	
	void setInputBuffer(byte[] inputBuffer) {
		this.inputBuffer = inputBuffer
		inputStream = new ByteArrayInputStream(inputBuffer)
	}

	String getOutput() {
		byteOutputStream.toString()
	}
	
	String getError() {
		byteErrorStream.toString()
	}
}
