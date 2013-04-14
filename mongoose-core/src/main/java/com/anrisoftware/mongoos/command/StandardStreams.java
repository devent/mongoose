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
package com.anrisoftware.mongoos.command;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.exec.PumpStreamHandler;

import com.anrisoftware.mongoose.api.commans.Command;

/**
 * Bundles the standard input and outputs of a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StandardStreams {

	public static final int STANDRD_INPUT_DESCRIPTOR = 0;

	public static final int STANDRD_OUTPUT_DESCRIPTOR = 1;

	public static final int STANDRD_ERROR_DESCRIPTOR = 2;

	private final StandardStreamsLogger log;

	private OutputStream inputStream;

	private InputStream outputStream;

	private InputStream errorStream;

	private PumpStreamHandler streams;

	/**
	 * Sets the standard input stream, standard output stream and standard error
	 * stream.
	 * 
	 * @param inputStream
	 *            the {@link OutputStream} for the standard input.
	 * 
	 * @param outputStream
	 *            the {@link InputStream} for the standard output.
	 * 
	 * @param errorStream
	 *            the {@link InputStream} for the standard error.
	 */
	public StandardStreams(OutputStream inputStream, InputStream outputStream,
			InputStream errorStream) {
		this.log = new StandardStreamsLogger();
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.errorStream = errorStream;
		this.streams = new PumpStreamHandler();
		streams.setProcessInputStream(inputStream);
		streams.setProcessOutputStream(outputStream);
		streams.setProcessErrorStream(errorStream);
	}

	/**
	 * Returns the stream handler.
	 * 
	 * @return the {@link PumpStreamHandler}.
	 */
	public PumpStreamHandler getStreamHandler() {
		return streams;
	}

	/**
	 * Sets the stream to write to the standard input of the command.
	 * 
	 * @param stream
	 *            the {@link OutputStream} stream.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	public void setInputStream(OutputStream stream) {
		log.checkInputStream(stream);
		this.inputStream = stream;
		streams.setProcessInputStream(stream);
	}

	/**
	 * @see Command#setInput(InputStream)
	 */
	public void setInputSource(InputStream stream) {
		log.checkSource(stream);
		streams = new PumpStreamHandler(System.out, System.err, stream);
		streams.setProcessOutputStream(outputStream);
		streams.setProcessErrorStream(errorStream);
	}

	/**
	 * Returns the stream to write to the standard input of the command.
	 * 
	 * @return the {@link OutputStream} stream.
	 */
	public OutputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Sets the stream that is connected with the standard output of the
	 * command.
	 * 
	 * @param stream
	 *            the {@link InputStream} for the standard output.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	public void setOutputStream(InputStream stream) {
		log.checkOutputStream(stream);
		this.outputStream = stream;
		streams.setProcessOutputStream(stream);
	}

	/**
	 * Returns the stream that is connected with the standard output of the
	 * command.
	 * 
	 * @return the {@link InputStream} for the standard output.
	 */
	public InputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @see Command#setOutput(int, OutputStream)
	 */
	public void setOutputTarget(int descriptor, OutputStream stream) {
		log.checkTarget(stream);
		switch (descriptor) {
		case STANDRD_OUTPUT_DESCRIPTOR:
			streams = new PumpStreamHandler(stream, System.err, System.in);
			streams.setProcessInputStream(inputStream);
			streams.setProcessErrorStream(errorStream);
			break;
		case 2:
			streams = new PumpStreamHandler(System.out, stream, System.in);
			streams.setProcessInputStream(inputStream);
			streams.setProcessOutputStream(outputStream);
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
			streams = new PumpStreamHandler(System.out, System.err, stream);
			streams.setProcessOutputStream(outputStream);
			streams.setProcessErrorStream(errorStream);
			break;
		default:
			throw log.unknownFileDescriptor(descriptor);
		}
	}

	/**
	 * Sets the stream that is connected with the standard error output of the
	 * command.
	 * 
	 * @param stream
	 *            the {@link InputStream} for the error output.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	public void setErrorStream(InputStream stream) {
		log.checkErrorStream(stream);
		this.errorStream = stream;
		streams.setProcessErrorStream(stream);
	}

	/**
	 * Returns the stream that is connected with the standard error output of
	 * the command.
	 * 
	 * @return the {@link InputStream} for the standard error.
	 */
	public InputStream getErrorStream() {
		return errorStream;
	}

	/**
	 * @see Command#setError(OutputStream)
	 */
	public void setErrorTarget(OutputStream stream) {
		log.checkTarget(stream);
		streams = new PumpStreamHandler(System.out, stream, System.in);
		streams.setProcessInputStream(inputStream);
		streams.setProcessOutputStream(outputStream);
	}

}
