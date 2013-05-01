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
class CommandPropertiesTest {

	@Test
	void "the environment"() {
		assert command.theEnvironment == environment
		shouldFailWith(ReadOnlyPropertyException) { command.theEnvironment = environment }
	}

	@Test
	void "the name"() {
		assert command.theName == command.name
		shouldFailWith(ReadOnlyPropertyException) { command.theName = "x" }
	}

	Environment environment

	CommandStub command

	StandardStreams streams

	def commandLogger

	@Before
	void setupCommand() {
		environment = new StubEnvironment()
		command = new CommandStub()
		streams = new StandardStreams()
		command.setAbstractCommandLogger new AbstractCommandLogger()
		command.setEnvironment environment
		command.setStreams streams
	}
}
