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
package com.anrisoftware.groovybash.buildins.echobuildin;

import static org.apache.commons.lang3.StringUtils.join;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.buildins.returns.ReturnCodeFactory;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * The build-in command {@code echo [nonewline] arguments…}. Outputs the
 * {@code arguments…}, separated by spaces. If {@code nonewline} was specified
 * the output is not followed by a newline, otherwise a newline is following the
 * output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoBuildin extends AbstractBuildin {

	static final String SEPARATOR = " ";

	private final ReturnCodeFactory returnCodeFactory;

	private EchoBuildin buildin;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	EchoBuildin(StandardStreams streams, ReturnCodeFactory returnCodeFactory) {
		super(streams);
		this.buildin = this;
		this.returnCodeFactory = returnCodeFactory;
	}

	protected EchoBuildin(EchoBuildin parent) {
		super(parent);
		this.returnCodeFactory = parent.returnCodeFactory;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		buildin.outputText(buildin.getOutput());
		return returnCodeFactory.createSuccess();
	}

	void outputText(String text) throws IOException {
		getOutputStream().println(text);
		getOutputStream().flush();
	}

	String getOutput() throws IOException {
		return join(getArgs(), SEPARATOR);
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (getFlag("nonewline", false)) {
			buildin = new EchoNoNewLine(this);
		}
		if (getFlag("in", null) != null && args.length == 0) {
			buildin = new FromInput(buildin);
		}
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "echo";
	}

	@Override
	public String toString() {
		return "echo";
	}
}
