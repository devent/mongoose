package com.anrisoftware.mongoose.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.mongoose.parameter.Parameter;

/**
 * Opens a reader to the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class OpenReader {

	private final OpenReaderLogger log;

	@Inject
	OpenReader(OpenReaderLogger logger) {
		this.log = logger;
	}

	/**
	 * Opens the reader depending if the script file or script resource was set.
	 * 
	 * @param parameter
	 *            the application {@link Parameter}.
	 * 
	 * @return the {@link Reader}.
	 * 
	 * @throws AppException
	 *             if there was an error opening the script file or resource.
	 */
	public Reader open(Parameter parameter) throws AppException {
		InputStream stream;
		if (parameter.getScriptFile() != null) {
			stream = openFile(parameter.getScriptFile());
		} else {
			stream = openResource(parameter.getScriptResource());
		}
		return new InputStreamReader(stream);
	}

	private InputStream openResource(URL url) throws AppException {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw log.errorOpenResource(e, url);
		}
	}

	private InputStream openFile(File file) throws AppException {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw log.errorOpenFile(e, file);
		}
	}
}
