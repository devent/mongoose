package com.anrisoftware.linuxdevices.command

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.junit.Before

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.linuxdevices.command.api.StandardStreams
import com.anrisoftware.linuxdevices.command.factories.CommandWorkerFactory
import com.anrisoftware.linuxdevices.command.factories.SuCommandWorkerFactory
import com.anrisoftware.linuxdevices.command.impl.CommandModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Setups the standard streams to test the command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CommandTestUtils extends TestUtils {

	def inputBuffer

	def inputStream

	def outputStream

	def errorStream

	def byteOutputStream

	def byteErrorStream

	Injector injector

	CommandWorkerFactory commandWorkerFactory

	SuCommandWorkerFactory asRootFactory

	ExecutorService executorService

	@Before
	void beforeTest() {
		createStandardStreams()
		injector = createInjector()
		executorService = Executors.newFixedThreadPool(3)
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
		Guice.createInjector new CommandModule()
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

	StandardStreams getStreams(def input=System.in) {
		return new StandardStreams(outputStream, errorStream, input)
	}
}
