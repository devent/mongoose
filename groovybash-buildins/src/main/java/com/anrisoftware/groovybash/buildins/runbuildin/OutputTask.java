package com.anrisoftware.groovybash.buildins.runbuildin;

import static java.lang.Thread.currentThread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Read data from an input and write the data to an output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class OutputTask implements Runnable {

	private final BufferedInputStream processOutput;

	private final PrintStream output;

	private Exception exception;

	private final int bufferSize;

	/**
	 * Sets the input and output streams.
	 * 
	 * @param input
	 *            the {@link InputStream} from which we read data.
	 * 
	 * @param output
	 *            the {@link PrintStream} to which we write the read data.
	 */
	@Inject
	OutputTask(@Assisted InputStream input, @Assisted PrintStream output) {
		this.output = output;
		this.bufferSize = 1024;
		this.processOutput = new BufferedInputStream(input);
	}

	/**
	 * Start reading data from the input and write the data to the output.
	 */
	@Override
	public void run() {
		try {
			readProcessOutput();
		} catch (Exception e) {
			exception = e;
		}
	}

	private void readProcessOutput() throws IOException, InterruptedException {
		byte[] buffer = new byte[bufferSize];
		int read;
		while ((read = processOutput.read(buffer)) != -1) {
			if (currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			output.write(buffer, 0, read);
		}
	}

	/**
	 * Returns the exception thrown.
	 * 
	 * @return the {@link Exception} thrown while reading or writing the data or
	 *         {@code null} if no exception was thrown.
	 */
	public Exception getException() {
		return exception;
	}

}
