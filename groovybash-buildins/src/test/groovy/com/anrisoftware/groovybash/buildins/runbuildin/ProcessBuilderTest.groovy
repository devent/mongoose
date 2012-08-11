package com.anrisoftware.groovybash.buildins.runbuildin

import org.junit.Test

/**
 * Test the usage of the {@link ProcessBuilder}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ProcessBuilderTest {

	@Test
	void "test run command with process builder"() {
		def builder = new ProcessBuilder("ls", "-al")
		builder.start()
	}
}
