package com.anrisoftware.groovybash.core.buildins.parsebuildin;

import static com.google.common.base.Preconditions.checkArgument;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ParseBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildinLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link ParseBuildin}.
	 */
	ParseBuildinLogger() {
		super(ParseBuildin.class);
	}

	void checkMinimumArgs(ParseBuildin buildin, Object[] args) {
		checkArgument(args.length > 0,
				"The build-in %s needs at least one argument.", buildin);
	}

	void errorParseArguments(CmdLineException e, String[] arguments) {
		log.error("Error parse command line arguments {}: {}.", arguments,
				e.getLocalizedMessage());
	}
}
