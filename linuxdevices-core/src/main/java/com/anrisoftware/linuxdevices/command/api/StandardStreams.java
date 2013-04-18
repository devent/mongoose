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
package com.anrisoftware.linuxdevices.command.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Bundles the standard input and outputs of a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class StandardStreams {

	public PrintStream outputStream;

	public PrintStream errorStream;

	public InputStream inputStream;

	/**
	 * Do not set any streams, the streams can to be set manually.
	 */
	public StandardStreams() {
	}

	/**
	 * Sets the standard input stream, standard output stream and standard error
	 * stream.
	 * 
	 * @param outputStream
	 *            the {@link PrintStream} for the standard output.
	 * 
	 * @param errorStream
	 *            the {@link PrintStream} for the standard error.
	 * 
	 * @param inputStream
	 *            the {@link InputStream} for the standard input.
	 */
	public StandardStreams(PrintStream outputStream, PrintStream errorStream,
			InputStream inputStream) {
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
	 * Sets the standard input stream.
	 * 
	 * @param stream
	 *            the {@link InputStream} for the standard input.
	 */
	public void setInputStream(InputStream stream) {
		this.inputStream = stream;
	}

	/**
	 * Creates an input stream from different sources and uses it as the
	 * standard input.
	 * 
	 * @param obj
	 *            the source {@link Object}.
	 * 
	 * @throws Exception
	 *             the there was an error opening or reading the source.
	 * 
	 */
	public void setInputStream(Object obj) throws Exception {
		if (obj instanceof File) {
			setInputStream((File) obj);
		} else if (obj instanceof InputStream) {
			setInputStream((InputStream) obj);
		} else {
			setInputStream(new File(obj.toString()));
		}
	}

	/**
	 * Read the file for the standard input.
	 * 
	 * @param file
	 *            the {@link File} to read.
	 * 
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public void setInputStream(File file) throws FileNotFoundException {
		inputStream = new FileInputStream(file);
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
	 * Sets the standard output stream.
	 * 
	 * @param stream
	 *            the {@link PrintStream} for the standard output.
	 */
	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Creates an input stream from different targets and uses it as the
	 * standard output.
	 * 
	 * @param obj
	 *            the target {@link Object}.
	 * 
	 * @throws Exception
	 *             the there was an error opening or writing to the target.
	 * 
	 */
	public void setOutputStream(Object obj) throws Exception {
		if (obj instanceof File) {
			setOutputStream((File) obj);
		} else if (obj instanceof OutputStream) {
			setOutputStream((OutputStream) obj);
		} else {
			setOutputStream(new File(obj.toString()));
		}
	}

	/**
	 * The file is used for the standard output.
	 * 
	 * @param file
	 *            the {@link File} to write to.
	 * 
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public void setOutputStream(File file) throws FileNotFoundException {
		outputStream = new PrintStream(file);
	}

	/**
	 * The stream is used for the standard output.
	 * 
	 * @param stream
	 *            the {@link OutputStream} to write to.
	 */
	public void setOutputStream(OutputStream stream) {
		outputStream = new PrintStream(stream);
	}

	/**
	 * Returns the standard error stream.
	 * 
	 * @return the {@link PrintStream} for the standard error.
	 */
	public PrintStream getErrorStream() {
		return errorStream;
	}

	/**
	 * Sets the standard error stream.
	 * 
	 * @param stream
	 *            the {@link PrintStream} for the error output.
	 */
	public void setErrorStream(PrintStream errorStream) {
		this.errorStream = errorStream;
	}

	/**
	 * Creates an input stream from different targets and uses it as the
	 * standard error.
	 * 
	 * @param obj
	 *            the target {@link Object}.
	 * 
	 * @throws Exception
	 *             the there was an error opening or writing to the target.
	 * 
	 */
	public void setErrorStream(Object obj) throws Exception {
		if (obj instanceof File) {
			setErrorStream((File) obj);
		} else if (obj instanceof OutputStream) {
			setErrorStream((OutputStream) obj);
		} else {
			setErrorStream(new File(obj.toString()));
		}
	}

	/**
	 * The file is used for the standard Error.
	 * 
	 * @param file
	 *            the {@link File} to write to.
	 * 
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public void setErrorStream(File file) throws FileNotFoundException {
		errorStream = new PrintStream(file);
	}

	/**
	 * The stream is used for the standard Error.
	 * 
	 * @param stream
	 *            the {@link OutputStream} to write to.
	 */
	public void setErrorStream(OutputStream stream) {
		outputStream = new PrintStream(stream);
	}

}
