package com.anrisoftware.groovybash.core.environment;

import static java.lang.String.format;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.groovybash.core.api.Buildin;

/**
 * Logging messages for {@link EnvironmentImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CallCommandWorkerLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link EnvironmentImpl}.
	 */
	CallCommandWorkerLogger() {
		super(EnvironmentImpl.class);
	}

	void workingDirectorySet(File directory) {
		log.info("Set working directory {}.", directory);
	}

	void errorCallBuildin(Buildin buildin, Exception e) {
		log.error(format("Build-in command %s throws an exception:", buildin),
				e);
	}
}
