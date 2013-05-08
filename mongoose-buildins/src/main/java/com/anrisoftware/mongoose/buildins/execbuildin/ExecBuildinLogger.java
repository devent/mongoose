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
package com.anrisoftware.mongoose.buildins.execbuildin;

import java.util.Map;

import org.apache.commons.exec.ExecuteException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link ExecBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecBuildinLogger extends AbstractLogger {

	private static final String SET_EXIT_VALUES = "Set success exit values to {} for {}.";
	private static final String SET_EXIT_VALUE = "Set success exit value to {} for {}.";
	private static final String SET_WORKING_DIRECTORY = "Set working directory to '{}' for {}.";
	private static final String SET_ENVIRONMENT = "Set environment {}.";
	private static final String SET_COMMAND = "Set command '{}'.";
	private static final String SET_WATCHDOG = "Set process watchdog {}.";
	private static final String SPECIFIED_WATCHDOG_TYPE_MESSAGE = "Specified watchdog %s is not of type ExecuteWatchdog";
	private static final String SPECIFIED_WATCHDOG_TYPE = "Specified watchdog is not of type ExecuteWatchdog";
	private static final String SET_DESTROYER = "Set process destroyer %s.";
	private static final String OBJECT = "object";
	private static final String SPECIFIED_DESTROYER_TYPE_MESSAGE = "Specified destroyer %s is not of type ProcessDestroyer";
	private static final String SPECIFIED_DESTROYER_TYPE = "Specified destroyer is not of type ProcessDestroyer";
	private static final String SET_HANDLER = "Set process handler {}.";
	private static final String SPECIFIED_HANDER_TYPE_MESSAGE = "Specified hander %s is not of type ExecuteResultHandler";
	private static final String SPECIFIED_HANDER_TYPE = "Specified hander is not of type ExecuteResultHandler";
	private static final String ERROR_INSTANTIATE_MESSAGE = "Error instantiate %s.";
	private static final String ERROR_INSTANTIATE = "Error instantiate";
	private static final String TYPE = "type";
	private static final String BUILDIN = "buildin";
	private static final String NO_DEFAULT_CONTRUCTOR_MESSAGE = "No default contructor found for %s.";
	private static final String NO_DEFAULT_CONTRUCTOR = "No default contructor";

	/**
	 * Creates a logger for {@link ExecBuildin}.
	 */
	ExecBuildinLogger() {
		super(ExecBuildin.class);
	}

	CommandException noDefaultCtor(ExecBuildin buildin,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(NO_DEFAULT_CONTRUCTOR, e)
				.addContext(BUILDIN, buildin).addContext(TYPE, type),
				NO_DEFAULT_CONTRUCTOR_MESSAGE, type);
	}

	CommandException errorInstantiate(ExecBuildin buildin,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(ERROR_INSTANTIATE, e)
				.addContext(BUILDIN, buildin).addContext(TYPE, type),
				ERROR_INSTANTIATE_MESSAGE, type);
	}

	CommandException errorHandlerType(ExecBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_HANDER_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_HANDER_TYPE_MESSAGE, object);
	}

	void handlerSet(ExecBuildin buildin, Object object) {
		log.debug(SET_HANDLER, object);
	}

	CommandException errorDestroyerType(ExecBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_DESTROYER_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_DESTROYER_TYPE_MESSAGE, object);
	}

	void destroyerSet(ExecBuildin buildin, Object object) {
		log.debug(SET_DESTROYER, object);
	}

	CommandException errorWatchdogType(ExecBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_WATCHDOG_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_WATCHDOG_TYPE_MESSAGE, object);
	}

	void watchdogSet(ExecBuildin buildin, Object object) {
		log.debug(SET_WATCHDOG, object);
	}

	void commandSet(ExecBuildin buildin, Object object) {
		log.debug(SET_COMMAND, object);
	}

	void envSet(ExecBuildin buildin, Map<String, String> env) {
		log.debug(SET_ENVIRONMENT, env);
	}

	void directorySet(ExecBuildin buildin, Object dir) {
		if (log.isDebugEnabled()) {
			log.debug(SET_WORKING_DIRECTORY, dir, buildin);
		} else {
			log.info(SET_WORKING_DIRECTORY, dir, buildin.getTheName());
		}
	}

	IllegalStateException notExitValueAvailable(ExecBuildin buildin) {
		return logException(
				new IllegalStateException(
						"No exit value is available, try ask your execute result handler."),
				"No exit value is available, try ask your execute result handler.");
	}

	CommandException errorCommand(ExecBuildin buildin, ExecuteException e) {
		return logException(new CommandException("Command returns with error",
				e).addContext(BUILDIN, buildin),
				"Command returns with error: '%s'", buildin.getTheName());
	}

	void exitValueSet(ExecBuildin buildin, int value) {
		if (log.isDebugEnabled()) {
			log.debug(SET_EXIT_VALUE, value, buildin);
		} else {
			log.info(SET_EXIT_VALUE, value, buildin.getTheName());
		}
	}

	void exitValuesSet(ExecBuildin buildin, int[] values) {
		if (log.isDebugEnabled()) {
			log.debug(SET_EXIT_VALUES, values, buildin);
		} else {
			log.info(SET_EXIT_VALUES, values, buildin.getTheName());
		}
	}
}
