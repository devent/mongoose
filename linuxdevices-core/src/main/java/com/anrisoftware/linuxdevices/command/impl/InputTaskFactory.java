package com.anrisoftware.linuxdevices.command.impl;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Factory to create the task that writes data to the input of a process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
interface InputTaskFactory {

	/**
	 * Create the task that writes data to the input of a process from the
	 * specified stream.
	 * 
	 * @param stream
	 *            the {@link OutputStream} of the input of the process.
	 * 
	 * @param output
	 *            the {@link InputStream} from which to read the data.
	 * 
	 * @return the {@link InputWorker}.
	 */
	InputWorker create(OutputStream stream, InputStream inputStream);
}
