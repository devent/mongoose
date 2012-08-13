package com.anrisoftware.groovybash.parameter

import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.spi.StringArrayOptionHandler

import com.anrisoftware.groovybash.core.factories.BashParserFactory
import com.anrisoftware.groovybash.parser.BashParser
import com.google.common.io.Files
import com.google.inject.Inject
import com.google.inject.Injector

/**
 * Parses the general parameter and the parameter according to the source
 * type.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParameterParser {

	private final ParameterParserLogger log

	private final Parameter parameter

	private final BashParserFactory bashParserFactory

	private BashParser bashParser

	/**
	 * Sets the parameter of the application.
	 * 
	 * @param parameter
	 * 			the {@link Parameter}.
	 */
	@Inject
	ParameterParser(ParameterParserLogger logger, Parameter parameter, BashParserFactory bashParserFactory) {
		this.log = logger
		this.parameter = parameter
		this.bashParserFactory = bashParserFactory
	}

	/**
	 * Parse the command line parameter.
	 * 
	 * @param args
	 * 			the array of the command line parameter.
	 * 
	 * @param injector
	 * 			the {@link Injector} to create a new project.
	 * 
	 * @return this {@link ParameterParser}.
	 */
	ParameterParser parseParameter(String[] args, Injector injector) {
		String[][] arguments = splitArguments(args)
		parseAppParameter(arguments[0])
		bashParser = bashParserFactory.create script
		bashParser.injector = injector
		bashParser.arguments = arguments[1]
		return this
	}
	
	private splitArguments(String[] args) {
		def i = args.findIndexOf { it == "--" }
		if (i < 0) {
			return [args, []]
		}
		if (args.length <= i + 1) {
			return [args[0..(i - 1)], []]
		}
		return [args[0..(i - 1)], args[(i + 1)..-1]]
	}

	private parseAppParameter(String[] args) {
		CmdLineParser.registerHandler(new String[0].getClass(), StringArrayOptionHandler.class)
		def parser = new CmdLineParser(parameter)
		log.parseArguments args
		parser.parseArgument args
	}

	private String getScript() {
		Files.toString parameter.script, parameter.charset
	}

	BashParser getBashParser() {
		bashParser
	}
}
