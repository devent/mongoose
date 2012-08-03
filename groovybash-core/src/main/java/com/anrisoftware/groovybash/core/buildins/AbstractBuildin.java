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

import static com.anrisoftware.groovybash.core.buildins.DefaultReturnValue.createSuccessValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.Environment;
import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.google.common.collect.Maps;

/**
 * Implements the standard input and output streams. Sets the arguments and
 * parse the optional flags.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractBuildin implements Buildin {

	private AbstractBuildinLogger log;

	private Object[] args;

	private Map<?, ?> flags;

	private Environment environment;

	private final StandardStreams streams;

	protected AbstractBuildin(AbstractBuildin parent) {
		this(parent.streams);
	}

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	protected AbstractBuildin(StandardStreams streams) {
		this.streams = streams;
		this.args = new Object[] {};
		this.flags = Maps.newHashMap();
	}

	/**
	 * Injects the logger.
	 * 
	 * @param logger
	 *            the {@link AbstractBuildinLogger}.
	 */
	@Inject
	public void setAbstractBuildinLogger(AbstractBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void setArguments(Object[] args) {
		if (args.length == 0) {
			return;
		}
		if (args[0] instanceof Map) {
			Map<?, ?> flags = (Map<?, ?>) args[0];
			setArguments(flags, args.length > 1 ? tail(args) : this.args);
		} else {
			this.args = args;
		}
	}

	private Object[] tail(Object[] args) {
		return ArrayUtils.subarray(args, 1, args.length);
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		this.flags = flags;
		this.args = args;
	}

	/**
	 * Returns the arguments for the build-in command.
	 * 
	 * @return the arguments array.
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * Return the flag with the specified key.
	 * 
	 * @param key
	 *            the key.
	 * 
	 * @param defaultValue
	 *            an optional default value for the flag.
	 * 
	 * @return the value of the flag or the specified default value if no such
	 *         flag with the specified key was found.
	 */
	public <T> T getFlag(Object key, T defaultValue) {
		// value in flags is of type T
		@SuppressWarnings("unchecked")
		T value = (T) flags.get(key);
		return value != null ? value : defaultValue;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		streams.inputStream = inputStream;
	}

	@Override
	public InputStream getInputStream() {
		return streams.inputStream;
	}

	@Override
	public void setOutputStream(PrintStream outputStream) {
		streams.outputStream = outputStream;
	}

	@Override
	public PrintStream getOutputStream() {
		return streams.outputStream;
	}

	@Override
	public void setErrorStream(PrintStream errorStream) {
		streams.errorStream = errorStream;
	}

	@Override
	public PrintStream getErrorStream() {
		return streams.errorStream;
	}

	public StandardStreams getStreams() {
		return streams;
	}

	@Override
	public ReturnValue call() throws Exception {
		setupInput();
		setupOutput();
		return createSuccessValue(streams.inputStream, streams.outputStream,
				streams.errorStream);
	}

	private void setupInput() throws FileNotFoundException {
		Object flag = getFlag("in", null);
		if (flag == null) {
			return;
		}
		setInput(flag);
	}

	private void setInput(Object flag) throws FileNotFoundException {
		if (flag instanceof File) {
			setInput((File) flag);
		} else if (flag instanceof InputStream) {
			setInput((InputStream) flag);
		} else {
			setInput(new File(flag.toString()));
		}
	}

	private void setInput(InputStream stream) throws FileNotFoundException {
		streams.inputStream = stream;
		log.inputStreamSet(this, stream);
	}

	private void setInput(File file) throws FileNotFoundException {
		streams.inputStream = new FileInputStream(file);
		log.inputFileSet(this, file);
	}

	private void setupOutput() throws FileNotFoundException {
		Object flag = getFlag("out", null);
		if (flag == null) {
			return;
		}
		setOutput(flag);
	}

	private void setOutput(Object flag) throws FileNotFoundException {
		if (flag instanceof File) {
			setOutput((File) flag);
		} else if (flag instanceof OutputStream) {
			setOutput((OutputStream) flag);
		} else {
			setOutput(new File(flag.toString()));
		}
	}

	private void setOutput(File file) throws FileNotFoundException {
		streams.outputStream = new PrintStream(file);
		log.outputFileSet(this, file);
	}

	private void setOutput(OutputStream stream) throws FileNotFoundException {
		streams.outputStream = new PrintStream(stream);
		log.outputStreamSet(this, stream);
	}

}
