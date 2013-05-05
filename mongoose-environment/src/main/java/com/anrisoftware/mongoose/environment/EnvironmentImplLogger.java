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
package com.anrisoftware.mongoose.environment;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CancellationException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.ExecutionMode;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link EnvironmentImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class EnvironmentImplLogger extends AbstractLogger {

	private static final String BUILD_IN_VARIABLE_READ_ONLY = "Build-in variable %s is read-only.";
	private static final String NO_BUILD_IN_VARIABLE = "No build-in variable %s.";
	private static final String SET_VARIABLE_INFO = "Set variable {} to '{}'.";
	private static final String SET_VARIABLE = "Set variable {} to '{}' for {}.";
	private static final String NO_COMMAND = "No command '%s' found.";
	private static final String EXECUTION_MODE_SET_INFO = "Set execution mode {}.";
	private static final String EXECUTION_MODE_SET = "Set execution mode {} for {}.";
	private static final String LOAD_COMMAND = "Load command '{}' for {}.";
	private static final String LOCALE_NULL = "The script locale cannot be null.";
	private static final String SET_LOCALE = "Set script locale {}.";
	private static final String CANCELED_COMMAND_MESSAGE = "Canceled command %s.";
	private static final String COMMAND_CANCELED = "Command canceled";
	private static final String COMMAND_ERROR_MESSAGE = "Error in command %s: %s";
	private static final String COMMAND_ERROR = "Command error";
	private static final String COMMAND = "command";
	private static final String COMMAND_INTERRUPTED_MESSAGE = "Interrupted command %s.";
	private static final String COMMAND_INTERRUPTED = "Command interrupted";
	private static final String SET_SCRIPT_HOME = "Set script home directory '{}'.";
	private static final String SET_ARGS = "Set command line arguments {}.";
	private static final String SET_WORKING_DIRECTORY = "Set working directory '{}'.";

	/**
	 * Creates logger for {@link EnvironmentImpl}.
	 */
	EnvironmentImplLogger() {
		super(EnvironmentImpl.class);
	}

	void workingDirectorySet(File directory) {
		log.debug(SET_WORKING_DIRECTORY, directory);
	}

	void argumentsSet(List<String> args) {
		log.debug(SET_ARGS, args);
	}

	void scriptHomeSet(File dir) {
		log.debug(SET_SCRIPT_HOME, dir);
	}

	CommandException commandInterrupted(InterruptedException e, Command command) {
		return logException(
				new CommandException(COMMAND_INTERRUPTED, e).addContext(
						COMMAND, command), COMMAND_INTERRUPTED_MESSAGE,
				command.getTheName());
	}

	CommandException commandError(Throwable e, Command command) {
		return logException(new CommandException(COMMAND_ERROR, e).addContext(
				COMMAND, command), COMMAND_ERROR_MESSAGE, command.getTheName(),
				e.getLocalizedMessage());
	}

	CommandException commandCanceled(CancellationException e, Command command) {
		return logException(
				new CommandException(COMMAND_CANCELED, e).addContext(COMMAND,
						command), CANCELED_COMMAND_MESSAGE,
				command.getTheName());
	}

	void localeSet(EnvironmentImpl env, Locale locale) {
		log.debug(SET_LOCALE, locale);
	}

	void checkLocale(EnvironmentImpl env, Locale locale) {
		notNull(locale, LOCALE_NULL);
	}

	void loadCommand(EnvironmentImpl environment, String name) {
		log.trace(LOAD_COMMAND, name, environment);
	}

	void executionModeSet(EnvironmentImpl environment, ExecutionMode mode) {
		if (log.isDebugEnabled()) {
			log.debug(EXECUTION_MODE_SET, mode, environment);
		} else {
			log.info(EXECUTION_MODE_SET_INFO, mode);
		}
	}

	void checkCommand(EnvironmentImpl environment, Command command, String name) {
		notNull(command, NO_COMMAND, name);
	}

	void checkVariable(boolean hasVariable, String name) {
		isTrue(hasVariable, NO_BUILD_IN_VARIABLE, name);
	}

	void variableSet(EnvironmentImpl environment, String name, Object value) {
		if (log.isDebugEnabled()) {
			log.debug(SET_VARIABLE, name, value, environment);
		} else {
			log.info(SET_VARIABLE_INFO, name, value);
		}
	}

	void checkVariableReadOnly(boolean containsKey, String name) {
		isTrue(containsKey, BUILD_IN_VARIABLE_READ_ONLY, name);
	}
}
