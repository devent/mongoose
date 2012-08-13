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

import java.nio.charset.Charset

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.buildins.BuildinsModule
import com.anrisoftware.groovybash.buildins.StandardStreams
import com.anrisoftware.groovybash.environment.EnvironmentModule
import com.anrisoftware.groovybash.executor.ExecutorModule
import com.anrisoftware.groovybash.parameter.ParameterModule
import com.anrisoftware.groovybash.parameter.ParameterParser
import com.anrisoftware.groovybash.parser.BashParser
import com.anrisoftware.groovybash.parser.ParserModule
import com.google.common.io.Files
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Setups the standard streams to test the build-in commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ScriptTestUtils extends TestUtils {

	def inputBuffer

	def inputStream

	def outputStream

	def errorStream
	
	def byteOutputStream

	def byteErrorStream
	
	Injector injector

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
		new ParserModule(),
		new EnvironmentModule(),
		new ExecutorModule(),
		new ParameterModule()
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
	
	/**
	 * Create a bash parser and run it with the specified script.
	 * 
	 * @param script
	 * 			  the script text to run.
	 * 
	 * @param args
	 * 			  the optional command line arguments for the script.
	 * 
	 * @param runCallback
	 * 			  an optional {@link Closure} in which context to run 
	 * 			  the script.
	 * 
	 * @param injector
	 * 			  the optional {@link Injector} which creates the 
	 * 			  bash parser.
	 * 
	 * @return the {@link BashParser}.
	 */
	BashParser runParser(String script, List args = [], def runCallback = {parser -> parser.run()}, Injector injector = injector) {
		def file = File.createTempFile("Script", "groovy")
		file.deleteOnExit()
		Files.write script, file, Charset.defaultCharset
		
		String[] arguments = ["-i", file, "--", args].flatten()
		ParameterParser parser = injector.getInstance ParameterParser
		parser.parseParameter arguments, injector
		runCallback(parser.bashParser)
	}
}
