package com.anrisoftware.groovybash.buildins.runbuildin

import org.junit.Test

import com.csvreader.CsvReader

/**
 * Test the splitting of the command string.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class SplitCommandTest {

	@Test
	void "split commands with whitespace quoted"() {
		Reader reader = new StringReader('bash -xc "echo Text"')
		CsvReader csv = new CsvReader(reader, ' 'as char)
		assert csv.readRecord()
		def values = csv.values
		assert values.length == 3
		assert values[0] == "bash"
		assert values[1] == "-xc"
		assert values[2] == "echo Text"
	}
}
