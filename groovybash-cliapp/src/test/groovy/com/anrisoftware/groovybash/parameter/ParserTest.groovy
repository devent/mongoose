package com.anrisoftware.groovybash.parameter

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.core.buildins.BuildinsModule
import com.anrisoftware.groovybash.core.environment.EnvironmentModule
import com.anrisoftware.groovybash.core.executor.ExecutorModule
import com.anrisoftware.groovybash.core.parser.ParserModule
import com.anrisoftware.groovybash.core.plugins.PluginsModule

class ParserTest extends CommandTestUtils {

	@Before
	void beforeTest() {
		modules = [new ParameterModule(), new BuildinsModule(), new ParserModule(),
						new EnvironmentModule(), new PluginsModule(),
						new ExecutorModule()]
		super.beforeTest()
	}

	@Test
	void "parse script"() {
		def file = File.createTempFile("EchoScript", "groovy")
		file.deleteOnExit()
		copyResourceToFile "com/anrisoftware/groovybash/parameter/EchoScript.groovy", file
		
		String[] args = ["-i", file]
		ParameterParser parser = injector.getInstance ParameterParser
		parser.parseParameter args, injector
		parser.bashParser.run()
	}
}
