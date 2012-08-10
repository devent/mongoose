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

import static org.apache.commons.lang3.StringUtils.join;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.Environment;
import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.returns.ReturnCodeFactory;
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

	protected ReturnCodeFactory returnCodeFactory;

	/**
	 * Sets the standard streams, the arguments and the flags from a parent
	 * build-in.
	 * 
	 * @param parent
	 *            the parent {@link AbstractBuildin}.
	 */
	protected AbstractBuildin(AbstractBuildin parent) {
		this(parent.streams);
		this.environment = parent.environment;
		this.args = parent.args;
		this.flags = parent.flags;
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

	/**
	 * Injects the factory to create a success return code.
	 * 
	 * @param returnCodeFactory
	 *            the {@link ReturnCodeFactory}.
	 */
	@Inject
	public void setReturnCodeFactory(ReturnCodeFactory returnCodeFactory) {
		this.returnCodeFactory = returnCodeFactory;
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
			setArguments(flags, args);
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

	@Override
	public ReturnValue call() throws Exception {
		setupInput();
		setupOutput();
		return returnCodeFactory.createSuccess();
	}

	private void setupInput() throws Exception {
		Object flag = getFlag("in", null);
		if (flag == null) {
			return;
		}
		streams.setInputStream(flag);
		log.inputStreamSet(this, flag);
	}

	private void setupOutput() throws Exception {
		Object flag = getFlag("out", null);
		if (flag == null) {
			return;
		}
		streams.setOutputStream(flag);
		log.outputStreamSet(this, flag);
	}

	@Override
	public String toString() {
		return String.format("%s %s \"%s\"", getName(), flags, join(args, " "));
	}
}
