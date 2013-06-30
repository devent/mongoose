package com.anrisoftware.mongoose.buildins.utils

import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.mongoose.api.commans.Command
import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities to test build-in commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BuildinsTestUtils {

	/**
	 * Creates the build-in command.
	 */
	static Command createCommand(Injector injector, Environment environment) {
		Command command = injector.getInstance(Command)
		command.setEnvironment environment
		return command
	}

	/**
	 * Creates the build-in command environment.
	 */
	static Environment createEnvironment(Injector injector) {
		injector.getInstance(Environment)
	}

	/**
	 * Creates the Guice injector.
	 */
	static Injector createInjector() {
		Guice.createInjector(
				new EnvironmentModule(),
				new PropertiesThreadsModule(),
				new ResourcesModule())
	}

	/**
	 * Returns the content of the stream.
	 */
	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}

	static {
		TestUtils.toStringStyle
	}
}
