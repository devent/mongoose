/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.buildins.parsebuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;
import groovy.lang.Closure;

import java.util.List;

import javax.inject.Singleton;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link ParseBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ParseBuildinLogger extends AbstractLogger {

	private static final String NOT_VALID_CLOSURE_SET = "Not valid closure set {} for {}.";
	private static final String NOT_VALID_CLOSURE_NULL = "Not valid closure cannot be null.";
	private static final String VALID_CLOSURE_SET = "Valid closure set {} for {}.";
	private static final String VALID_CLOSURE_NULL = "Valid closure cannot be null.";
	private static final String ARGUMENTS = "arguments";
	private static final String ERROR_PARSE_ARGUMENTS_MESSAGE = "Error parse arguments %s";
	private static final String ERROR_PARSE_ARGUMENTS = "Error parse arguments";
	private static final String PARAMETER_SET = "Parameter set {} for {}";
	private static final String ARGUMENTS_SET = "Arguments set {} for {}.";
	private static final String ARGUMENTS_NULL = "The arguments cannot be null.";
	private static final String EXPECTING_PARAMETER_TO_PARSE = "Expecting the parameter to parse.";

	/**
	 * Creates a logger for {@link ParseBuildin}.
	 */
	ParseBuildinLogger() {
		super(ParseBuildin.class);
	}

	CommandException errorParseArguments(CmdLineException e,
			List<String> arguments) {
		return logException(
				new CommandException(ERROR_PARSE_ARGUMENTS, e).add(
						ARGUMENTS, arguments), ERROR_PARSE_ARGUMENTS_MESSAGE,
				arguments);
	}

	void checkParameter(ParseBuildin buildin, List<Object> args) {
		isTrue(args.size() > 0, EXPECTING_PARAMETER_TO_PARSE);
	}

	void checkArguments(ParseBuildin buildin, List<String> arguments) {
		notNull(arguments, ARGUMENTS_NULL);
	}

	void argumentsSet(ParseBuildin buildin, List<String> arguments) {
		log.debug(ARGUMENTS_SET, arguments, buildin);
	}

	void parameterSet(ParseBuildin buildin, Object parameter) {
		log.debug(PARAMETER_SET, parameter, buildin);
	}

	void checkValid(ParseBuildin buildin, Closure<?> closure) {
		notNull(closure, VALID_CLOSURE_NULL);
	}

	void validSet(ParseBuildin buildin, Closure<?> closure) {
		log.debug(VALID_CLOSURE_SET, closure, buildin);
	}

	void checkNotValid(ParseBuildin buildin, Closure<?> closure) {
		notNull(closure, NOT_VALID_CLOSURE_NULL);
	}

	void notValidSet(ParseBuildin buildin, Closure<?> closure) {
		log.debug(NOT_VALID_CLOSURE_SET, closure, buildin);
	}

}
