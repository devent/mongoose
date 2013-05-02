package com.anrisoftware.mongoose.parameter;

import org.apache.commons.lang3.ArrayUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ParameterParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParameterParserLogger extends AbstractLogger {

	private static final String SCRIPT_FILE_OR_RESOURCE_REQUIRED = "The script file or script resource are required.";
	private static final String PARSE_COMMAND_LINE_ARGUMENTS = "Parse the command line arguments {}.";

	/**
	 * Create a logger for {@link ParameterParser}.
	 */
	ParameterParserLogger() {
		super(ParameterParser.class);
	}

	void parseArguments(String[] arguments) {
		log.debug(PARSE_COMMAND_LINE_ARGUMENTS, ArrayUtils.toString(arguments));
	}

	void checkParameter(ParameterImpl parameter, CmdLineParser parser)
			throws CmdLineException {
		if (parameter.getScriptFile() == null
				&& parameter.getScriptResource() == null) {
			throw logException(new CmdLineException(parser,
					SCRIPT_FILE_OR_RESOURCE_REQUIRED),
					SCRIPT_FILE_OR_RESOURCE_REQUIRED);
		}
	}
}
