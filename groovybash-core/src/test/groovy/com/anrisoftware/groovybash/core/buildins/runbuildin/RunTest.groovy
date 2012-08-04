package com.anrisoftware.groovybash.core.buildins.runbuildin

import org.junit.Test

class RunTest {

	@Test
	void "test run command with process builder"() {
		def builder = new ProcessBuilder("ls", "-al")
		builder.start()
	}
}
