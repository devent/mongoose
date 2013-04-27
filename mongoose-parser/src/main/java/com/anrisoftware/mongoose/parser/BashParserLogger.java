package com.anrisoftware.mongoose.parser;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BashParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.3
 */
class BashParserLogger extends AbstractLogger {

	private static final String START_SCRIPT = "Start script {}.";

	private static final String SHUTDOWN_SCRIPT = "Shutdown script {}, canceled commands: {}.";

	/**
	 * Creates logger for {@link BashParser}.
	 */
	BashParserLogger() {
		super(BashParser.class);
	}

	void startScript(BashParser parser) {
		if (log.isDebugEnabled()) {
			log.debug(START_SCRIPT, parser);
		} else {
			log.debug(START_SCRIPT, parser.getEnvironment().getScriptHome());
		}
	}

	void shutdownScript(BashParser parser) {
		if (log.isDebugEnabled()) {
			log.debug(SHUTDOWN_SCRIPT, parser, parser.getCanceledCommands());
		} else {
			log.debug(SHUTDOWN_SCRIPT, parser.getEnvironment().getScriptHome(),
					parser.getCanceledCommands());
		}
	}
}
