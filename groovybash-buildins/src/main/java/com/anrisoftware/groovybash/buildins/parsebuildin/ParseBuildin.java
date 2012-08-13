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
package com.anrisoftware.groovybash.buildins.parsebuildin;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * The build-in command {@code parse obj [args…]}.
 * <p>
 * Parse the specified command line arguments {@code args…}. If no arguments are
 * specified it will use the command line arguments of the script, stored in the
 * {@code ARGS} build-in variable. {@code obj} needs to be an instance of a
 * class annotated with {@link Option} or {@link Argument}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParseBuildin extends AbstractBuildin {

	private final ParseBuildinLogger log;

	private CmdLineParser parser;

	private String[] arguments;

	private final ParameterValueFactory valueFactory;

	private Object bean;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	ParseBuildin(ParseBuildinLogger logger, StandardStreams streams,
			ParameterValueFactory returnValueFactory) {
		super(streams);
		this.log = logger;
		this.valueFactory = returnValueFactory;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		try {
			parser.parseArgument(arguments);
		} catch (CmdLineException e) {
			log.errorParseArguments(e, arguments);
			return createReturnValue(false);
		}
		return createReturnValue(true);
	}

	private ParameterValue createReturnValue(boolean valid) {
		return valueFactory.create(getOutputStream(), parser, bean, valid);
	}

	@Override
	protected void setupArguments() throws Exception {
		Object[] args = getArgs();
		log.checkMinimumArgs(this, args);
		bean = args[0];
		parser = new CmdLineParser(bean);
		arguments = createArguments(args);
	}

	private String[] createArguments(Object[] args) {
		if (args.length < 2) {
			return getEnvironment().getArguments();
		}
		if (args[1] instanceof List) {
			return argumentsAsList(args[1]);
		} else {
			return parseArguments(args[1].toString());
		}
	}

	private String[] parseArguments(String string) {
		return StringUtils.split(string, " ");
	}

	private String[] argumentsAsList(Object args) {
		List<Object> list = asList(args);
		String[] arguments = new String[list.size()];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = list.get(i).toString();
		}
		return arguments;
	}

	@SuppressWarnings("unchecked")
	private List<Object> asList(Object obj) {
		return (List<Object>) obj;
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "parse";
	}
}
