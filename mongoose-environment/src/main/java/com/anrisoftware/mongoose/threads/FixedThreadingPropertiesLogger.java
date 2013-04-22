package com.anrisoftware.mongoose.threads;

import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link FixedThreadingProperties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class FixedThreadingPropertiesLogger extends AbstractLogger {

	private static final String MAXIMUM_NUMBER_NULL = "No maximum number of threads property found.";

	/**
	 * Create logger for {@link FixedThreadingProperties}.
	 */
	public FixedThreadingPropertiesLogger() {
		super(FixedThreadingProperties.class);
	}

	void checkMaxThreads(FixedThreadingProperties p, Number value) {
		notNull(value, MAXIMUM_NUMBER_NULL);
	}
}
