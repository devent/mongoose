package com.anrisoftware.mongoose.buildins.sudobuildin;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link SudoBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class SudoBuildinLogger extends AbstractLogger {

	private static final String BACKEND_TYPE_MESSAGE = "Backend %s is not of type SudoBackend";
	private static final String BACKEND_TYPE = "Backend is not of type SudoBackend";
	private static final String BUILDIN = "build-in";
	private static final String OBJECT = "object";
	private static final String HANDLER_SET = "Backend set to {} for {}.";

	/**
	 * Create logger for {@link SudoBuildin}.
	 */
	SudoBuildinLogger() {
		super(SudoBuildin.class);
	}

	CommandException errorBackendType(SudoBuildin buildin, Object object) {
		return logException(
				new CommandException(BACKEND_TYPE).addContext(BUILDIN, buildin)
						.addContext(OBJECT, object), BACKEND_TYPE_MESSAGE,
				object);
	}

	void backendSet(SudoBuildin buildin, Object object) {
		if (log.isDebugEnabled()) {
			log.debug(HANDLER_SET, object, buildin);
		} else {
			log.info(HANDLER_SET, object, buildin.getTheName());
		}
	}
}
