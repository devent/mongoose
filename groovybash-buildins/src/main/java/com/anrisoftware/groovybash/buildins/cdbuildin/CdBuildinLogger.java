package com.anrisoftware.groovybash.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFoundException;

/**
 * Logging messages for {@link CdBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class CdBuildinLogger extends AbstractLogger {

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
}
