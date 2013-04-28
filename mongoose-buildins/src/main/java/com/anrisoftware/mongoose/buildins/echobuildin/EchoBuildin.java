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
package com.anrisoftware.mongoose.buildins.echobuildin;

import static java.lang.Boolean.FALSE;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.mongoose.api.exceptions.ExecutionException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code echo}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoBuildin extends AbstractCommand {

	private static final String NEWLINE = "newline";

	private static final String SEPARATOR = " ";

	private OutputWorker output;

	private String text;

	EchoBuildin() {
		this.output = createOutputNewLine();
		this.text = "";
	}

	@Override
	public EchoBuildin call() throws ExecutionException {
		PrintStream stream = new PrintStream(getOutput());
		output.output(stream, text);
		stream.flush();
		return this;
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) {
		setupNamed(args);
		setupUnnamed(unnamedArgs);
	}

	private void setupUnnamed(List<Object> unnamedArgs) {
		text = StringUtils.join(unnamedArgs, SEPARATOR);
	}

	private void setupNamed(Map<String, Object> args) {
		if (args.containsKey(NEWLINE)) {
			if (((Boolean) args.get(NEWLINE)).equals(FALSE)) {
				output = createOutputNoNewLine();
			} else {
				output = createOutputNewLine();
			}
		}
	}

	private OutputWorker createOutputNoNewLine() {
		return new OutputWorker() {

			@Override
			public void output(PrintStream output, String text) {
				output.print(text);
			}
		};
	}

	private OutputWorker createOutputNewLine() {
		return new OutputWorker() {

			@Override
			public void output(PrintStream output, String text) {
				output.println(text);
			}

		};
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getTheName() {
		return EchoService.ID;
	}
}
