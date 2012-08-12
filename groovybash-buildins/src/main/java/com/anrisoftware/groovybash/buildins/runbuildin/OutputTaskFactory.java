package com.anrisoftware.groovybash.buildins.runbuildin;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Factory to create the task that reads data from the input and writes the data
 * to the output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
interface OutputTaskFactory {

	/**
	 * Creates the task that reads data from the input and writes the data to
	 * the output.
	 * 
	 * @param input
	 *            the {@link InputStream} from which we read data.
	 * 
	 * @param output
	 *            the {@link PrintStream} to which we write the read data.
	 * 
	 * @return the {@link OutputTask}.
	 */
	OutputTask create(InputStream inputStream, PrintStream outputStream);
}
