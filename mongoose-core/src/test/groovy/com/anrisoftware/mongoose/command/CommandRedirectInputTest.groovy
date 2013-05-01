package com.anrisoftware.mongoose.command

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test

import com.anrisoftware.mongoose.api.commans.Environment

/**
 * @see AbstractCommand
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandRedirectInputTest {

	@Test
	void "input property [file source]"() {
		String string = "test"
		fileSource string, {
			command.input = it
			command.call()
			assert command.readData == string
		}
	}

	@Test
	void "input property [stream source]"() {
		String string = "test"
		def stream = new ByteArrayInputStream(string.getBytes())
		command.input = stream
		command()
		assert command.readData == string
	}

	@Test
	void "input operation [file source]"() {
		String string = "test"
		fileSource string, {
			command = command.input it
			command.call()
			assert command.readData == string
		}
	}

	@Test
	void "input operation [stream source]"() {
		String string = "test"
		def stream = new ByteArrayInputStream(string.getBytes())
		command.input stream
		command()
		assert command.readData == string
	}

	@Test
	void "<< operation [file source]"() {
		String string = "test"
		fileSource string, {
			command = command << it
			assert command.readData == string
		}
	}

	@Test
	void "<< operation [stream source]"() {
		String string = "test"
		def stream = new ByteArrayInputStream(string.getBytes())
		command = command << stream
		assert command.readData == string
	}

	Environment environment

	CommandStub command

	StandardStreams streams

	@Before
	void setupCommand() {
		environment = new StubEnvironment()
		command = new CommandStub() {

					String readData

					@Override
					void doCall() {
						readData = IOUtils.toString input
					}
				}
		streams = new StandardStreams()
		command.setAbstractCommandLogger new AbstractCommandLogger()
		command.setEnvironment environment
		command.setStreams streams
	}

	static fileSource(String string, def test) {
		File tmpfile = File.createTempFile("inputfile", string)
		try {
			FileUtils.write tmpfile, string
			test(tmpfile)
		} finally {
			tmpfile.delete()
		}
	}
}
