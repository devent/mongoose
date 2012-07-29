package com.anrisoftware.groovybash.core.buildins;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Bundles the standart input and outputs of a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StandardStreams {

	private final InputStream inputStream;

	private final PrintStream outputStream;

	private final PrintStream errorStream;

	/**
	 * Sets the standard input stream, standard output stream and standard error
	 * stream.
	 * 
	 * @param inputStream
	 *            the {@link InputStream} for the standard input.
	 * 
	 * @param outputStream
	 *            the {@link PrintStream} for the standard output.
	 * 
	 * @param errorStream
	 *            the {@link PrintStream} for the standard error.
	 */
	public StandardStreams(InputStream inputStream, PrintStream outputStream,
			PrintStream errorStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.errorStream = errorStream;
	}

	/**
	 * Returns the standard input stream.
	 * 
	 * @return the {@link InputStream} for the standard input.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Returns the standard output stream.
	 * 
	 * @return the {@link PrintStream} for the standard output.
	 */
	public PrintStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Returns the standard error stream.
	 * 
	 * @return the {@link PrintStream} for the standard error.
	 */
	public PrintStream getErrorStream() {
		return errorStream;
	}
}
