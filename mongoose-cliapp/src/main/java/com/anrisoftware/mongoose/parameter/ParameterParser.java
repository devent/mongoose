package com.anrisoftware.mongoose.parameter;

import static org.kohsuke.args4j.CmdLineParser.registerHandler;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import com.google.inject.Inject;

/**
 * Parses the general parameter and the parameter according to the source type.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ParameterParser {

	private static final Class<? extends String[]> STRING_ARRAY_CLASS = new String[0]
			.getClass();

	private final ParameterParserLogger log;

	private final ParameterImpl parameter;

	private final SplitArgs splitArgs;

	/**
	 * Parses the parameter.
	 */
	@Inject
	ParameterParser(ParameterParserLogger logger, ParameterImpl parameter,
			SplitArgs splitArgs) {
		this.log = logger;
		this.parameter = parameter;
		this.splitArgs = splitArgs;
	}

	/**
	 * Parse the command line parameter.
	 * 
	 * @param args
	 *            the array of the command line parameter.
	 * 
	 * @return this {@link ParameterParser}.
	 * 
	 * @throws CmdLineException
	 *             if the arguments are not valid.
	 */
	public ParameterParser parseParameter(String[] args)
			throws CmdLineException {
		String[][] arguments = splitArgs.split(args);
		parseAppParameter(arguments[0]);
		parameter.setArgs(arguments[1]);
		return this;
	}

	private void parseAppParameter(String[] args) throws CmdLineException {
		registerHandler(STRING_ARRAY_CLASS, StringArrayOptionHandler.class);
		CmdLineParser parser = new CmdLineParser(parameter);
		log.parseArguments(args);
		parser.parseArgument(args);
	}

	/**
	 * Returns the parsed command line parameter.
	 * 
	 * @return the {@link Parameter}.
	 */
	public Parameter getParameter() {
		return parameter;
	}
}
