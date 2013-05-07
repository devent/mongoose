package com.anrisoftware.mongoose.environment

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.parser.ParserModule
import com.anrisoftware.mongoose.parser.ScriptParserFactory
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see EnvironmentImpl
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EnvironmentTest {

	@Test(timeout = 1000l)
	void "variable [env]"() {
		def script = scriptFactory.create createReader("""
println ENV
println ENV["PATH"]
println ENV.PATH

ENV["PATH"] = "/tmp"
assert ENV["PATH"] == "/tmp"
ENV = [MY: "my"]
"""), "Test"
		script.setEnvironment environment
		script()
	}

	Injector injector

	ScriptParserFactory scriptFactory

	Environment environment

	@Before
	void setupApp() {
		injector = Guice.createInjector(new ParserModule(), new EnvironmentModule(), new ResourcesModule(), new ThreadsModule())
		scriptFactory = injector.getInstance(ScriptParserFactory)
		environment = injector.getInstance(Environment)
	}

	@BeforeClass
	static void setupStringStyle() {
		toStringStyle
	}

	static Reader createReader(String text) {
		new StringReader(text)
	}
}
