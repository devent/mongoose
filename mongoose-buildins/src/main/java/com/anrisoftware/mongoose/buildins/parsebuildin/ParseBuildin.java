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

import groovy.lang.Closure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.mongoos.command.AbstractCommand;
import com.anrisoftware.mongoose.api.commans.Environment;

/**
 * The build-in command {@code parse}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParseBuildin extends AbstractCommand {

	private final ParseBuildinLogger log;

	private List<String> arguments;

	private Object parameter;

	private Closure<?> valid;

	private Closure<?> notValid;

	@SuppressWarnings("serial")
	@Inject
	ParseBuildin(ParseBuildinLogger logger) {
		this.log = logger;
		this.arguments = null;
		this.parameter = null;
		this.valid = new Closure<Void>(this) {
		};
		this.notValid = new Closure<Void>(this) {
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
		setArguments(Arrays.asList(environment.getArguments()));
	}

	@Override
	public ParseBuildin call() throws Exception {
		try {
			CmdLineParser parser = new CmdLineParser(parameter);
			parser.parseArgument(arguments);
			valid.call(parameter);
		} catch (CmdLineException e) {
			notValid.call();
			throw log.errorParseArguments(e, arguments);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		setParameter(unnamedArgs);
		if (args.containsKey("arguments")) {
			setArguments((List<String>) args.get("arguments"));
		}
		if (args.containsKey("valid")) {
			setValid((Closure<?>) args.get("valid"));
		}
		if (args.containsKey("notValid")) {
			setNotValid((Closure<?>) args.get("notValid"));
		}
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
		this.arguments = arguments;
		log.argumentsSet(this, arguments);
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

	private void setParameter(List<Object> args) {
		log.checkParameter(this, args);
		Object parameter = args.get(0);
		this.parameter = parameter;
		log.parameterSet(this, parameter);
	}

}
