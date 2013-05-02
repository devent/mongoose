package com.anrisoftware.mongoose.parser;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ScriptParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.3
 */
class ScriptParserLogger extends AbstractLogger {

	private static final String START_SCRIPT = "Start script {}.";

	private static final String SHUTDOWN_SCRIPT = "Shutdown script {}, canceled commands: {}.";

	/**
	 * Creates logger for {@link ScriptParser}.
	 */
	ScriptParserLogger() {
		super(ScriptParser.class);
	}

	void startScript(ScriptParser parser) {
		if (log.isDebugEnabled()) {
			log.debug(START_SCRIPT, parser);
		} else {
			log.debug(START_SCRIPT, parser.getEnvironment().getScriptHome());
		}
	}

	void shutdownScript(ScriptParser parser) {
		if (log.isDebugEnabled()) {
			log.debug(SHUTDOWN_SCRIPT, parser, parser.getCanceledCommands());
		} else {
			log.debug(SHUTDOWN_SCRIPT, parser.getEnvironment().getScriptHome(),
					parser.getCanceledCommands());
		}
	}
}
