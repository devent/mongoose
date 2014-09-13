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

	@Option(name = "-file", aliases = ["-f"])
	File file = false
}

def start(Args args) {
    exec "gs -q -o nul -sDEVICE=nullpage -dFirstPage=1 -dLastPage=1 ${args.file}"
}
