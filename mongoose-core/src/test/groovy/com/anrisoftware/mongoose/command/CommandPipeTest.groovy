package com.anrisoftware.mongoose.command

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.mongoose.api.commans.Environment

/**
 * @see AbstractCommand
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandPipeTest {

	@Test
	void "pipe commands"() {
		commandReader = commandWriter | commandReader
		assert commandReader.readData == writeData
	}

	String writeData

	Environment environment

	CommandStub commandWriter

	CommandStub commandReader

	StandardStreams streams

	@Before
	void setupCommand() {
		writeData = "test"
		environment = new StubEnvironment()
		commandWriter = new CommandWriterStub(writeData)
		commandReader = new CommandReaderStub()
		commandReader.setAbstractCommandLogger new AbstractCommandLogger()
		commandReader.setEnvironment environment
		commandReader.setStreams new StandardStreams()
		commandWriter.setAbstractCommandLogger new AbstractCommandLogger()
		commandWriter.setEnvironment environment
		commandWriter.setStreams new StandardStreams()
	}

	static {
		toStringStyle
	}
}
