package com.anrisoftware.mongoose.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.DirectoryNotFoundException;

/**
 * Logging messages for {@link CdBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CdBuildinLogger extends AbstractLogger {

	private static final String CHANGED_DIRECTORY = "Changed directory to '{}'.";

	/**
	 * Create logger for {@link CdBuildin}.
	 */
	CdBuildinLogger() {
		super(CdBuildin.class);
	}

	void checkDirectory(File directory) throws DirectoryNotFoundException {
		if (!directory.isDirectory()) {
			throw new DirectoryNotFoundException(directory);
		}
	}

	void changedDirectory(File directory) {
		log.debug(CHANGED_DIRECTORY, directory);
	}
}
