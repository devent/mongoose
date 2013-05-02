package com.anrisoftware.mongoose.app;

import java.util.Arrays;

import javax.inject.Singleton;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link App}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AppLogger extends AbstractLogger {

	private static final String ERROR_ARGUMENTS = "Error parse arguments";
	private static final String ERROR_ARGUMENTS_MESSAGE = "Error parse command line arguments %s.";
	private static final String ERROR_SCRIPT = "Error execute script";
	private static final String ERROR_SCRIPT_MESSAGE = "Error execute script";

	/**
	 * Create logger for {@link App}.
	 */
	public AppLogger() {
		super(App.class);
	}

	AppException errorParseArgs(CmdLineException e, String[] args) {
		return logException(new AppException(ERROR_ARGUMENTS, e),
				ERROR_ARGUMENTS_MESSAGE, Arrays.toString(args));
	}

	AppException errorScript(Exception e) {
		return logException(new AppException(ERROR_SCRIPT, e),
				ERROR_SCRIPT_MESSAGE);
	}
}
