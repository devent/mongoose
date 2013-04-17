package com.anrisoftware.mongoose.buildins.logbuildins;

import static java.lang.Integer.MAX_VALUE;
import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractLogBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AbstractLogBuildinLogger extends AbstractLogger {

	private static final String SET_ARGUMENTS = "Set arguments {} for {}.";
	private static final String SET_MESSAGE = "Set message '{}' for {}.";
	private static final String MESSAGE_CANNOT_NULL = "The message cannot be null.";
	private static final String NEED_LOGGING_MESSAGE = "Need at least the logging message.";

	/**
	 * Creates logger for {@link AbstractLogBuildin}.
	 * 
	 * @param contextClass
	 *            the context {@link Class}.
	 */
	AbstractLogBuildinLogger() {
		super(AbstractLogBuildin.class);
	}

	void checkArgs(AbstractLogBuildin buildin, List<Object> args) {
		inclusiveBetween(1, MAX_VALUE, args.size(), NEED_LOGGING_MESSAGE);
	}

	void checkMessage(AbstractLogBuildin buildin, Object object) {
		notNull(object, MESSAGE_CANNOT_NULL);
	}

	void messageSet(AbstractLogBuildin buildin, String message) {
		log.debug(SET_MESSAGE, message, buildin);
	}

	void argumentsSet(AbstractLogBuildin buildin, Object[] arguments) {
		log.debug(SET_ARGUMENTS, arguments, buildin);
	}
}
