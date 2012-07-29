package com.anrisoftware.groovybash.core

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.groovybash.core.buildins.BuildinsModule
import com.anrisoftware.groovybash.core.buildins.StandardStreams
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
}
