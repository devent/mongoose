package com.anrisoftware.mongoose.parameter

import javax.inject.Singleton

/**
 * Split the arguments in a group before the delimeter and a group
 * after the delimeter.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class SplitArgs {

	/**
	 * If the arguments contains {@code --} split the arguments in a group
	 * of arguments before the {@code --} and in a group of arguments after.
	 * 
	 * @param args
	 * 		  the arguments.
	 * 
	 * @return the group before the delimiter and the group after the delimiter.
	 */
	String[][] split(String[] args) {
		def i = args.findIndexOf { it == "--" }
		if (i < 0) {
			return [args, []]
		}
		if (args.length <= i + 1) {
			return [args[0..(i - 1)], []]
		}
		return [
			args[0..(i - 1)],
			args[(i + 1)..-1]
		]
	}
}
