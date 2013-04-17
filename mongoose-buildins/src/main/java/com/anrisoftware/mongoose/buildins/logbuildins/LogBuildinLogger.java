package com.anrisoftware.mongoose.buildins.logbuildins;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link LogBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class LogBuildinLogger extends AbstractLogger {

	private static final String NO_LOGGER_CREATED = "No logger created for %s.";
	private static final String ONE_ARGUMENT = "Expects only one argument.";

	/**
	 * Create logger for {@link LogBuildin}.
	 */
	public LogBuildinLogger() {
		super(LogBuildin.class);
	}

	void checkArgs(LogBuildin buildin, Map<String, Object> args) {
		inclusiveBetween(1, 1, args.size(), ONE_ARGUMENT);
	}

	void checkLogger(LogBuildin buildin, Object logger) {
		notNull(logger, NO_LOGGER_CREATED, buildin);
	}
}
