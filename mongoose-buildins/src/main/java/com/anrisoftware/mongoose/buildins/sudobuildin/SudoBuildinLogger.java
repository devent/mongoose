package com.anrisoftware.mongoose.buildins.sudobuildin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

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

	private static final String ERROR_SET_PROPERTY_MESSAGE = "Error set property '%s' to backend %s";
	private static final String ERROR_SET_PROPERTY = "Error set property to backend";
	private static final String BACKEND_TYPE_MESSAGE = "Backend %s is not of type SudoBackend";
	private static final String BACKEND_TYPE = "Backend is not of type SudoBackend";
	private static final String BUILDIN = "build-in";
	private static final String OBJECT = "object";
	private static final String BACKEND_SET = "Backend set {} for {}.";

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
			log.debug(BACKEND_SET, object, buildin);
		} else {
			log.info(BACKEND_SET, object, buildin.getTheName());
		}
	}

	PropertyVetoException errorSetupProperties(SudoBuildin buildin,
			Exception e, PropertyChangeEvent evt) {
		return logException(new PropertyVetoException(ERROR_SET_PROPERTY, evt),
				ERROR_SET_PROPERTY_MESSAGE, evt.getPropertyName(), buildin);
	}
}
