package com.anrisoftware.groovybash.core.api;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

/**
 * Groovy Bash command build-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Buildin extends Callable<Buildin> {

	/**
	 * Execute the build-in command.
	 */
	@Override
	Buildin call() throws Exception;

	/**
	 * Sets the arguments for the build-in command.
	 * 
	 * @param args
	 *            the arguments.
	 */
	void setArguments(Object... args);

	/**
	 * Sets the standard input stream.
	 * 
	 * @param stream
	 *            the {@link InputStream} for standard input.
	 */
	void setInputStream(InputStream stream);

	/**
	 * Returns the standard input stream.
	 * 
	 * @return the {@link InputStream} for standard input.
	 */
	InputStream getInputStream();

	/**
	 * Sets the standard output stream.
	 * 
	 * @param stream
	 *            the {@link PrintStream} for standard output.
	 */
	void setOutputStream(PrintStream stream);

	/**
	 * Returns the standard output stream.
	 * 
	 * @return the {@link PrintStream} for standard output.
	 */
	PrintStream getOutputStream();

	/**
	 * Sets the standard error stream.
	 * 
	 * @param stream
	 *            the {@link PrintStream} for the standard errors.
	 */
	void setErrorStream(PrintStream stream);

	/**
	 * Returns the standard error stream.
	 * 
	 * @return the {@link PrintStream} for standard errors.
	 */
	PrintStream getErrorStream();
}
