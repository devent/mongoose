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
package com.anrisoftware.mongoose.api.exceptions;

import java.io.IOException;
import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Thrown if the executed command returns with an error exit value.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ExecutionException extends CommandException {

	private static final String EXIT_VALUE_KEY = "exit value";

	private Context<ExecutionException> context;

	/**
	 * Sets the exit value that caused the exception.
	 * 
	 * @param exitValue
	 *            the exit value.
	 * 
	 * @see IOException#IOException(String, Throwable)
	 */
	public ExecutionException(String message, Throwable cause, int exitValue) {
		super(message, cause);
		add(EXIT_VALUE_KEY, exitValue);
	}

	/**
	 * Sets the return code that caused the exception.
	 * 
	 * @param exitValue
	 *            the exit value.
	 * 
	 * @see IOException#IOException(String)
	 */
	public ExecutionException(String message, int exitValue) {
		super(message);
		add(EXIT_VALUE_KEY, exitValue);
	}

	/**
	 * Returns the exit value that caused the exception.
	 * 
	 * @return the exit value.
	 */
	public int getTheValue() {
		return (Integer) context.getContext().get(EXIT_VALUE_KEY);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	@Override
	public ExecutionException add(String name, Object value) {
		return context.addContext(name, value);
	}

	/**
	 * @see Context#getContext()
	 */
	@Override
	public Map<String, Object> getContext() {
		return context.getContext();
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
