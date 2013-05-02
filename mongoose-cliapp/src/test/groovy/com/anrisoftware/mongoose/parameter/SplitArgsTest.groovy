package com.anrisoftware.mongoose.parameter

import org.junit.Test

/**
 * @see SplitArgs
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SplitArgsTest {

	@Test
	void "split args"() {
		inputs.each {
			def result = split.split(it.args as String[])
			assert result.size() == 2
			List resultA = result[0]
			assert resultA.containsAll(it.groupA)
			List resultB = result[1]
			assert resultB.containsAll(it.groupB)
		}
	}

	static SplitArgs split = new SplitArgs()

	static inputs = [
		[args: ["-a", "--", "-b"], groupA: ["-a"], groupB: ["-b"]],
		[args: ["-a"], groupA: ["-a"], groupB: []],
		[
			args: ["-a", "-b"],
			groupA: ["-a", "-b"],
			groupB: []],
		[
			args: ["-a", "--"],
			groupA: ["-a"],
			groupB: []]
	]
}
