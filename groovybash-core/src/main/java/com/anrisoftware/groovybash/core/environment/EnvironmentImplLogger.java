package com.anrisoftware.groovybash.core.environment;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link EnvironmentImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EnvironmentImplLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link EnvironmentImpl}.
	 */
	EnvironmentImplLogger() {
		super(EnvironmentImpl.class);
	}

	void workingDirectorySet(File directory) {
		log.info("Set working directory {}.", directory);
	}
}
