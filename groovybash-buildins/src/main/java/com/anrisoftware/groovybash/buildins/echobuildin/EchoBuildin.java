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
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.common.io.CharStreams;

/**
 * The build-in command {@code echo [noNewline: true|false] arguments…}. Outputs
 * the {@code arguments…}, separated by spaces. If {@code noNewline} is set to
 * {@code true} the output is not followed by a newline, otherwise a newline is
 * following the output. {@code noNewline} is set to {@code false} as default.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class EchoBuildin extends AbstractBuildin {

	private static final String NONEWLINE = "noNewline";

	private static final String SEPARATOR = " ";

	private OutputWorker output;

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
		this.output = createOutput();
	}

	private OutputWorker createOutput() {
		return new OutputWorker() {

			@Override
			public void output(PrintStream output, String text) {
				output.println(text);
			}
		};
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		output.output(getOutputStream(), getOutput());
		getOutputStream().flush();
		return returnCodeFactory.createSuccess();
	}

	private String getOutput() {
		Object[] args = getArgs();
		if (args.length == 0) {
			return "";
		}
		return join(args, SEPARATOR);
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (getFlag(NONEWLINE, false)) {
			output = createOutputNoNewline();
		}
		if (getFlag("in", null) != null && args.length == 0) {
			output = createOutputFromInputStream();
		}
	}

	private OutputWorker createOutputFromInputStream() {
		return new OutputWorker() {

			@Override
			public void output(PrintStream output, String a) throws IOException {
				InputStreamReader i = new InputStreamReader(getInputStream());
				String text = CharStreams.toString(i);
				output.print(text);
			}
		};
	}

	private OutputWorker createOutputNoNewline() {
		return new OutputWorker() {

			@Override
			public void output(PrintStream output, String text) {
				output.print(text);
			}
		};
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "echo";
	}
}
