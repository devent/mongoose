package com.anrisoftware.mongoose.app

import org.junit.Test

import com.anrisoftware.mongoose.parameter.Parameter

/**
 * @see FileName
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FileNameTest {

	@Test
	void "split args"() {
		inputs.each {
			def result = fileName.name(it.parameter)
			assert result == it.fileName
		}
	}

	static FileName fileName = new FileName()

	static inputs = [
		[parameter: [getScriptFile: { new File("/tmp/test.groovy") }, getScriptResource: { null }] as Parameter, fileName: "test.groovy"],
		[parameter: [getScriptFile: { }, getScriptResource: { new URL("http://localhost/test.groovy") }] as Parameter, fileName: "test.groovy"],
		[parameter: [getScriptFile: { }, getScriptResource: { new URL("http://google.com/test.groovy") }] as Parameter, fileName: "test.groovy"],
	]
}
