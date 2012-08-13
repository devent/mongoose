package com.anrisoftware.groovybash.buildins.logbuildins;

import static com.google.common.base.Preconditions.checkArgument;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractLogBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class LogBuildinLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link AbstractLogBuildin}.
	 * 
	 * @param contextClass
	 *            the context {@link Class}.
	 */
	LogBuildinLogger() {
		super(AbstractLogBuildin.class);
	}

	void checkMinimumArgs(AbstractLogBuildin buildin, Object[] args) {
		checkArgument(args.length > 0,
				"The logger %s needs at least the message.", buildin);
	}
}
