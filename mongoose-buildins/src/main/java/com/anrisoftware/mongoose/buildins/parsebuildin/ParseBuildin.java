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
package com.anrisoftware.mongoose.buildins.parsebuildin;

import static java.util.Collections.unmodifiableList;
import groovy.lang.Closure;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code parse}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildin extends AbstractCommand {

	private static final String NOT_VALID_KEY = "notValid";

	private static final String VALID_KEY = "valid";

	private static final String ARGUMENTS_KEY = "arguments";

	private final ParseBuildinLogger log;

	private List<String> arguments;

	private Object parameter;

	private Closure<?> valid;

	private Closure<?> notValid;

	private CmdLineParser parser;

	@SuppressWarnings("serial")
	@Inject
	ParseBuildin(ParseBuildinLogger logger) {
		this.log = logger;
		this.arguments = null;
		this.parameter = null;
		this.valid = new Closure<Void>(this) {
			@Override
			public Void call(Object arguments) {
				return null;
			}
		};
		this.notValid = new Closure<Void>(this) {
			@Override
			public Void call() {
				return null;
			}
		};
	}

	/**
	 * Returns the name {@code parse}.
	 */
	@Override
	public String getTheName() {
		return "parse";
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		setArguments(environment.getArgs());
	}

	@Override
	protected void doCall() throws Exception {
		try {
			parser.parseArgument(arguments);
			valid.call(parameter);
		} catch (CmdLineException e) {
			notValid.call();
			throw log.errorParseArguments(e, arguments);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		setupParameter(unnamedArgs);
		if (args.containsKey(ARGUMENTS_KEY)) {
			setArguments((List<String>) args.get(ARGUMENTS_KEY));
		}
		if (args.containsKey(VALID_KEY)) {
			setValid((Closure<?>) args.get(VALID_KEY));
		}
		if (args.containsKey(NOT_VALID_KEY)) {
			setNotValid((Closure<?>) args.get(NOT_VALID_KEY));
		}
	}

	private void setupParameter(List<Object> args) {
		log.checkParameter(this, args);
		Object parameter = args.get(0);
		this.parameter = parameter;
		this.parser = new CmdLineParser(parameter);
		log.parameterSet(this, parameter);
	}

	/**
	 * Sets the command line arguments.
	 * 
	 * @param arguments
	 *            the {@link List} with the arguments.
	 * 
	 * @throws NullPointerException
	 *             if the specified arguments are {@code null}.
	 */
	public void setArguments(List<String> arguments) {
		log.checkArguments(this, arguments);
		this.arguments = copyArgs(arguments);
		log.argumentsSet(this, arguments);
	}

	private List<String> copyArgs(List<?> list) {
		List<String> args = new ArrayList<String>();
		for (Object item : list) {
			args.add(item.toString());
		}
		return unmodifiableList(args);
	}

	/**
	 * Returns the command line arguments.
	 * 
	 * @returns the {@link List} with the arguments.
	 */
	public List<String> getArguments() {
		return arguments;
	}

	/**
	 * Sets the closure that is called if the command line arguments are parsed
	 * without any errors. The first argument of the closure will be the parsed
	 * parameter.
	 * 
	 * @param closure
	 *            the {@link Closure}.
	 * 
	 * @throws NullPointerException
	 *             if the specified closure is {@code null}.
	 */
	public void setValid(Closure<?> closure) {
		log.checkValid(this, closure);
		this.valid = closure;
		log.validSet(this, closure);
	}

	/**
	 * Returns the closure that is called if the command line arguments are
	 * parsed without any errors.
	 * 
	 * @return the {@link Closure}.
	 */
	public Closure<?> getValid() {
		return valid;
	}

	/**
	 * Sets the closure that is called if the command line arguments are parsed
	 * with errors.
	 * 
	 * @param closure
	 *            the {@link Closure}.
	 * 
	 * @throws NullPointerException
	 *             if the specified closure is {@code null}.
	 */
	public void setNotValid(Closure<?> closure) {
		log.checkNotValid(this, closure);
		this.notValid = closure;
		log.notValidSet(this, closure);
	}

	/**
	 * Returns the closure that is called if the command line arguments are
	 * parsed with errors.
	 * 
	 * @return the {@link Closure}.
	 */
	public Closure<?> getNotValid() {
		return notValid;
	}

	/**
	 * Returns the parameter bean object.
	 * 
	 * @return the parameter bean {@link Object}.
	 */
	public Object getTheParameter() {
		return parameter;
	}

	/**
	 * Returns an example of the command line arguments.
	 * 
	 * @param exampleMode
	 *            the {@link ExampleMode}.
	 * 
	 * @return the example {@link String}.
	 */
	public String printExample(ExampleMode exampleMode) {
		return parser.printExample(exampleMode);
	}

	/**
	 * Returns an example of the command line arguments.
	 * 
	 * @param exampleMode
	 *            the {@link ExampleMode}.
	 * 
	 * @param resourceBundle
	 *            the {@link ResourceBundle} for the text descriptions or
	 *            {@code null}.
	 * 
	 * @return the example {@link String}.
	 */
	public String printExample(ExampleMode exampleMode,
			ResourceBundle resourceBundle) {
		return resourceBundle == null ? parser.printExample(exampleMode)
				: parser.printExample(exampleMode, resourceBundle);
	}

	/**
	 * Returns an example of the usage in a single line.
	 * 
	 * @return the example {@link String}.
	 */
	public String printSingleLineUsage() {
		return printSingleLineUsage(null);
	}

	/**
	 * Returns an example of the usage in a single line.
	 * 
	 * @param resourceBundle
	 *            the {@link ResourceBundle} for the text descriptions or
	 *            {@code null}.
	 * 
	 * @return the example {@link String}.
	 */
	public String printSingleLineUsage(ResourceBundle resourceBundle) {
		StringWriter writer = new StringWriter();
		parser.printSingleLineUsage(writer, resourceBundle);
		return writer.toString();
	}

	/**
	 * Returns an example of the usage.
	 * 
	 * @return the example {@link String}.
	 */
	public String printUsage() {
		return printUsage(null);
	}

	/**
	 * Returns an example of the usage.
	 * 
	 * @param resourceBundle
	 *            the {@link ResourceBundle} for the text descriptions or
	 *            {@code null}.
	 * 
	 * @return the example {@link String}.
	 */
	public String printUsage(ResourceBundle resourceBundle) {
		StringWriter writer = new StringWriter();
		parser.printUsage(writer, resourceBundle);
		return writer.toString();
	}
}
