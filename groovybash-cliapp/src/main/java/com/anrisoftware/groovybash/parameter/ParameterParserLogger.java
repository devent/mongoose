package com.anrisoftware.groovybash.parameter;

import org.apache.commons.lang3.ArrayUtils;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ParameterParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParameterParserLogger extends AbstractLogger {

	/**
	 * Create a logger for {@link ParameterParser}.
	 */
	ParameterParserLogger() {
		super(ParameterParser.class);
	}

	void parseArguments(String[] arguments) {
		log.debug("Parse the command line arguments {}.",
				ArrayUtils.toString(arguments));
	}
}
