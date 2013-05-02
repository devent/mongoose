package com.anrisoftware.mongoose.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link OpenReader}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class OpenReaderLogger extends AbstractLogger {

	private static final String ERROR_FILE_MESSAGE = "Error open script file '%s'";
	private static final String ERROR_FILE = "Error open script file";
	private static final String ERROR_RESOURCE = "Error open script resource";
	private static final String ERROR_RESOURCE_MESSAGE = "Error open script resource %s";

	/**
	 * Create logger for {@link OpenReader}.
	 */
	public OpenReaderLogger() {
		super(OpenReader.class);
	}

	AppException errorOpenFile(FileNotFoundException e, File file) {
		return logException(new AppException(ERROR_FILE, e),
				ERROR_FILE_MESSAGE, file);
	}

	AppException errorOpenResource(IOException e, URL url) {
		return logException(new AppException(ERROR_RESOURCE, e),
				ERROR_RESOURCE_MESSAGE, url);
	}

}
