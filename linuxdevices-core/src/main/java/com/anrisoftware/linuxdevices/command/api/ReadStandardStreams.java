package com.anrisoftware.linuxdevices.command.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.inject.Inject;

/**
 * Redirects the output and error stream to memory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class ReadStandardStreams extends StandardStreams {

	private final ByteArrayOutputStream byteOutputStream;

	private final ByteArrayOutputStream byteErrorStream;

	/**
	 * Redirects the output and error stream to memory.
	 */
	@Inject
	ReadStandardStreams() {
		this.byteOutputStream = new ByteArrayOutputStream();
		this.byteErrorStream = new ByteArrayOutputStream();
		this.outputStream = new PrintStream(byteOutputStream);
		this.errorStream = new PrintStream(byteErrorStream);
	}

	/**
	 * Returns the content of the output stream.
	 * 
	 * @return the content of the output stream.
	 */
	public String getOutput() {
		return byteOutputStream.toString();
	}

	/**
	 * Returns the content of the error stream.
	 * 
	 * @return the content of the error stream.
	 */
	public String readError() {
		return byteOutputStream.toString();
	}
}
