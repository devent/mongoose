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
package com.anrisoftware.groovybash.core.buildins.echobuildin;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.DefaultReturnValue;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

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

	private static final String SEPARATOR = " ";

	private EchoBuildin buidlin;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	EchoBuildin(StandardStreams streams) {
		super(streams);
		this.buidlin = this;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		return buidlin.callBuildin();
	}

	ReturnValue callBuildin() {
		getOutputStream().println(join(getArgs(), SEPARATOR));
		getOutputStream().flush();
		return DefaultReturnValue.createSuccessValue(getInputStream(),
				getOutputStream(), getErrorStream());
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (getFlag("nonewline", false)) {
			buidlin = nonewline();
		}
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "echo";
	}

	/**
	 * Specify that no newline should be followed the specified arguments.
	 * 
	 * @return this {@link EchoBuildin}.
	 */
	public EchoBuildin nonewline() {
		return new EchoNoNewLine(this);
	}

	/**
	 * The {@code echo} build-in command that will not output a newline after
	 * the arguments.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 1.0
	 */
	private static class EchoNoNewLine extends EchoBuildin {

		private EchoNoNewLine(AbstractBuildin parent) {
			super(new StandardStreams(parent.getInputStream(),
					parent.getOutputStream(), parent.getErrorStream()));
			setArguments(parent.getArgs());
		}

		@Override
		ReturnValue callBuildin() {
			getOutputStream().print(join(getArgs(), SEPARATOR));
			getOutputStream().flush();
			return DefaultReturnValue.createSuccessValue(getInputStream(),
					getOutputStream(), getErrorStream());
		}
	}

	@Override
	public String toString() {
		return "echo";
	}
}
