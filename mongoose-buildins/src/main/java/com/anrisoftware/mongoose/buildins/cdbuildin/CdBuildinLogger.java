package com.anrisoftware.mongoose.buildins.cdbuildin;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.DirectoryNotFoundException;

/**
 * Logging messages for {@link CdBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CdBuildinLogger extends AbstractLogger {

	private static final String ARGUMENTS = "Can have only zero or one arguments.";
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

	void checkArguments(CdBuildin buildin, int size) {
		inclusiveBetween(0, 1, size, ARGUMENTS);
	}
}
