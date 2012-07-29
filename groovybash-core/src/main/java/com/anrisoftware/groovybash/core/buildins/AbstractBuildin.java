package com.anrisoftware.groovybash.core.buildins;

import java.io.InputStream;
import java.io.PrintStream;

import com.anrisoftware.groovybash.core.api.Buildin;

/**
 * Implements the standard input and output streams.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractBuildin implements Buildin {

	private InputStream inputStream;

	private PrintStream outputStream;

	private PrintStream errorStream;

	private Object[] args;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	protected AbstractBuildin(StandardStreams streams) {
		this.inputStream = streams.getInputStream();
		this.outputStream = streams.getOutputStream();
		this.errorStream = streams.getErrorStream();
	}

	@Override
	public void setArguments(Object... args) {
		this.args = args;
	}

	/**
	 * Returns the arguments for the build-in command.
	 * 
	 * @return the arguments array.
	 */
	protected Object[] getArgs() {
		return args;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public PrintStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void setErrorStream(PrintStream errorStream) {
		this.errorStream = errorStream;
	}

	@Override
	public PrintStream getErrorStream() {
		return errorStream;
	}

}
