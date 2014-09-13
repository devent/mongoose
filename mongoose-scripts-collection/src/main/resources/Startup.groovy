#!/usr/bin/env rungroovybash
package com.anrisoftware.groovybash.startup

import static org.apache.commons.io.FileUtils.*
import static org.apache.commons.io.FilenameUtils.*

parse valid: { start(it) },
notValid: { printHelp() },
new Args()
return 0

/**
 * Prints the manual page.
 */
void printHelp() {
	def programname = 'Startup.groovy'
	echo TEMPLATES.Documentation.man_page(
			"programname", programname,
			"singleLineUsage", parameter.singleLineUsage)
}

class Args {

	@Option(name = "-auto", aliases = ["-A"])
	boolean auto = false
}

def start(Args args) {

}
