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
package com.anrisoftware.groovybash.core

import net.xeoh.plugins.base.PluginManager
import net.xeoh.plugins.base.impl.PluginManagerFactory
import net.xeoh.plugins.base.util.JSPFProperties
import net.xeoh.plugins.base.util.uri.ClassURI

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.core.buildins.BuildinsModule
import com.anrisoftware.groovybash.core.buildins.StandardStreams
import com.anrisoftware.groovybash.core.factories.BashParserFactory
import com.anrisoftware.groovybash.core.parser.BashParser
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Setups the standard streams to test commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandTestUtils extends TestUtils {

	def inputBuffer

	def inputStream

	def outputStream

	def errorStream
	
	def byteOutputStream

	def byteErrorStream
	
	Injector injector

	def cacheEnabled = "false"
	
	def cacheMode = "weak"
	
	def cacheFile = "jspf.cache"
	
	def cacheLoggingLevel = "WARNING"

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
		inputBuffer = []as byte[]
		inputStream = new ByteArrayInputStream(inputBuffer)
		byteOutputStream = new ByteArrayOutputStream(1024)
		outputStream = new PrintStream(byteOutputStream)
		byteErrorStream = new ByteArrayOutputStream(1024)
		errorStream = new PrintStream(byteErrorStream)
		injector = createInjector()
	}
	
	Injector createInjector() {
		Guice.createInjector new BuildinsTestModule(
			inputStream: inputStream, outputStream: outputStream, errorStream: errorStream)
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
	
	PluginManager createPluginManager() {
		def props = new JSPFProperties()
		props.setProperty PluginManager, "cache.enabled", cacheEnabled
		props.setProperty PluginManager, "cache.mode", cacheMode
		props.setProperty PluginManager, "cache.file", cacheFile
		props.setProperty PluginManager, "logging.level", cacheLoggingLevel

		def manager = PluginManagerFactory.createPluginManager(props)
		manager.addPluginsFrom ClassURI.CLASSPATH
		manager
	}

	/**
	 * Create a bash parser and run it with the specified script.
	 * 
	 * @param script
	 * 			  the script to run.
	 * 
	 * @param runClosure
	 * 			  an optional {@link Closure} in which context to run 
	 * 			  the parser.
	 * 
	 * @param args
	 * 			  the optional command line arguments for the script.
	 * 
	 * @param injector
	 * 			  the optional {@link Injector} which creates the 
	 * 			  bash parser.
	 * 
	 * @return the {@link BashParser}.
	 */
	BashParser runParser(String script, def runClosure = null, List args = [], Injector injector = injector) {
		BashParserFactory factory = injector.getInstance BashParserFactory
		BashParser parser = factory.create script
		parser.injector = injector
		parser.arguments = args as String[]
		if (runClosure != null) {
			runClosure(parser)
		} else {
			parser.run()
		}
		parser
	}

}
