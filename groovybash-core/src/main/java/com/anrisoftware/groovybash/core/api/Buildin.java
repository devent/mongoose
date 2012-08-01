/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.core.api;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Groovy Bash command build-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Buildin extends Callable<ReturnValue> {

	/**
	 * Execute the build-in command.
	 * 
	 * @return the {@link ReturnValue} of the command.
	 * 
	 * @throws Exception
	 *             if there was an error executing the command.
	 */
	@Override
	ReturnValue call() throws Exception;

	/**
	 * Sets the environment of the build-in command. The environment contains
	 * the global variables.
	 * 
	 * @param environment
	 *            the {@link Environment}.
	 */
	void setEnvironment(Environment environment);

	/**
	 * Returns the environment of the build-in command. The environment contains
	 * the global variables.
	 * 
	 * @return the {@link Environment}.
	 */
	Environment getEnvironment();

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the name of the build-in command.
	 */
	String getName();

	/**
	 * Sets the arguments for the build-in command.
	 * 
	 * @param args
	 *            the arguments.
	 */
	void setArguments(Object[] args);

	/**
	 * Sets the arguments for the build-in command.
	 * 
	 * @param flags
	 *            a {@link Map} with the flags for the build-in command.
	 * 
	 * @param args
	 *            the arguments.
	 */
	void setArguments(Map<?, ?> flags, Object[] args);

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
