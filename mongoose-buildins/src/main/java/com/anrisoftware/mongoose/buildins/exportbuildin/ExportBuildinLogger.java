package com.anrisoftware.mongoose.buildins.exportbuildin;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ExportBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ExportBuildinLogger extends AbstractLogger {

	private static final String SET_REMOVE_INFO = "Set remove environment variable {}.";
	private static final String SET_REMOVE = "Set remove {} for {}.";

	/**
	 * Create logger for {@link ExportBuildin}.
	 */
	ExportBuildinLogger() {
		super(ExportBuildin.class);
	}

	void removeSet(ExportBuildin buildin, boolean remove) {
		if (log.isDebugEnabled()) {
			log.debug(SET_REMOVE, remove, buildin);
		} else {
			log.info(SET_REMOVE_INFO, remove);
		}
	}
}
