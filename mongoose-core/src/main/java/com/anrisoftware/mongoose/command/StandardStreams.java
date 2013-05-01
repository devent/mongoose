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
package com.anrisoftware.mongoose.command;

import java.io.InputStream;
import java.io.OutputStream;

import com.anrisoftware.mongoose.api.commans.Command;

/**
 * Bundles the standard input and outputs of a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StandardStreams {

	/**
	 * The file descriptor for the standard input.
	 */
	public static final int STANDRD_INPUT_DESCRIPTOR = 0;

	/**
	 * The file descriptor for the standard output.
	 */
	public static final int STANDRD_OUTPUT_DESCRIPTOR = 1;

	/**
	 * The file descriptor for the standard error.
	 */
	public static final int STANDRD_ERROR_DESCRIPTOR = 2;

	private final StandardStreamsLogger log;

	private InputStream inputSource;

	private OutputStream outputTarget;

	private OutputStream errorTarget;

	/**
	 * Sets the default standard input stream, standard output stream and
	 * standard error stream.
	 * 
	 * @see System#in
	 * @see System#out
	 * @see System#err
	 */
	public StandardStreams() {
		this(System.in, System.out, System.err);
	}

	/**
	 * @see StandardStreams#StandardStreams()
	 * 
	 * @param inputSource
	 *            the input source {@link InputStream}.
	 * 
	 * @param outputTarget
	 *            the output target {@link OutputStream}.
	 * 
	 * @param errorTarget
	 *            the error output target {@link OutputStream}.
	 * 
	 */
	public StandardStreams(InputStream inputSource, OutputStream outputTarget,
			OutputStream errorTarget) {
		this.log = new StandardStreamsLogger();
		this.inputSource = inputSource;
		this.outputTarget = outputTarget;
		this.errorTarget = errorTarget;
	}

	/**
	 * @see Command#setInput(InputStream)
	 */
	public void setInputSource(InputStream stream) {
		log.checkSource(stream);
		this.inputSource = stream;
	}

	/**
	 * @see Command#getInput()
	 */
	public InputStream getInputSource() {
		return inputSource;
	}

	/**
	 * @see Command#setOutput(int, OutputStream)
	 */
	public void setOutputTarget(int descriptor, OutputStream stream) {
		log.checkTarget(stream);
		switch (descriptor) {
		case STANDRD_OUTPUT_DESCRIPTOR:
			this.outputTarget = stream;
			break;
		case STANDRD_ERROR_DESCRIPTOR:
			break;
		default:
			throw log.unknownFileDescriptor(descriptor);
		}
	}

	/**
	 * @see Command#setOutput(int, InputStream)
	 */
	public void setOutputTarget(int descriptor, InputStream stream) {
		log.checkTarget(stream);
		switch (descriptor) {
		case STANDRD_INPUT_DESCRIPTOR:
			this.inputSource = stream;
			break;
		default:
			throw log.unknownFileDescriptor(descriptor);
		}
	}

	/**
	 * @see Command#getOutput()
	 */
	public OutputStream getOutputTarget() {
		return outputTarget;
	}

	/**
	 * @see Command#setError(OutputStream)
	 */
	public void setErrorTarget(OutputStream stream) {
		log.checkTarget(stream);
		this.errorTarget = stream;
	}

	/**
	 * @see Command#getError()
	 */
	public OutputStream getErrorTarget() {
		return errorTarget;
	}

}
