package com.anrisoftware.mongoose.buildins.pwdbuildin;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link PwdBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PwdBuildinLogger extends AbstractLogger {

	private static final String ARGUMENTS = "Does not take any arguments.";

	/**
	 * Create logger for {@link PwdBuildin}.
	 */
	PwdBuildinLogger() {
		super(PwdBuildin.class);
	}

	void checkArguments(PwdBuildin buildin, int size) {
		inclusiveBetween(0, 0, size, ARGUMENTS);
	}
}
