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
