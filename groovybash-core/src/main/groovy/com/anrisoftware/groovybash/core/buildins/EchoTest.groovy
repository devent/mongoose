package com.anrisoftware.groovybash.core.buildins

import org.junit.Test

class EchoTest {

	static final echoNewline = """
echo
"""

	@Test
	void "echo newline"() {
		Script dslScript = new GroovyShell().parse(echoNewline)
	}
}
