package com.anrisoftware.groovybash.core.buildins;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.google.common.collect.Maps;

/**
 * Implements the standard input and output streams.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractBuildin implements Buildin {

	private InputStream inputStream;

	private PrintStream outputStream;

	private PrintStream errorStream;

	private Object[] args;

	private Map<?, ?> flags;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	protected AbstractBuildin(StandardStreams streams) {
		this.inputStream = streams.getInputStream();
		this.outputStream = streams.getOutputStream();
		this.errorStream = streams.getErrorStream();
		this.args = new Object[] {};
		this.flags = Maps.newHashMap();
	}

	@Override
	public void setArguments(Object[] args) {
		if (args[0] instanceof Map) {
			setArguments((Map<?, ?>) args[0],
					ArrayUtils.subarray(args, 1, args.length));
		} else {
			this.args = args;
		}
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
	protected Object[] getArgs() {
		return args;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getFlag(Object key, T defaultValue) {
		T value = (T) flags.get(key);
		return value != null ? value : defaultValue;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public PrintStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void setErrorStream(PrintStream errorStream) {
		this.errorStream = errorStream;
	}

	@Override
	public PrintStream getErrorStream() {
		return errorStream;
	}

}
