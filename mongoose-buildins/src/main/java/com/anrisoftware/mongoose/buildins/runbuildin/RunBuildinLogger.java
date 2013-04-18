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
package com.anrisoftware.mongoose.buildins.runbuildin;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link RunBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RunBuildinLogger extends AbstractLogger {

	private static final String SET_ENVIRONMENT = "Set environment %s.";
	private static final String SET_COMMAND = "Set command '%s'.";
	private static final String SET_WATCHDOG = "Set process watchdog %s.";
	private static final String SPECIFIED_WATCHDOG_TYPE_MESSAGE = "Specified watchdog %s is not of type ExecuteWatchdog";
	private static final String SPECIFIED_WATCHDOG_TYPE = "Specified watchdog is not of type ExecuteWatchdog";
	private static final String SET_DESTROYER = "Set process destroyer %s.";
	private static final String OBJECT = "object";
	private static final String SPECIFIED_DESTROYER_TYPE_MESSAGE = "Specified destroyer %s is not of type ProcessDestroyer";
	private static final String SPECIFIED_DESTROYER_TYPE = "Specified destroyer is not of type ProcessDestroyer";
	private static final String SET_HANDLER = "Set process handler %s.";
	private static final String SPECIFIED_HANDER_TYPE_MESSAGE = "Specified hander %s is not of type ExecuteResultHandler";
	private static final String SPECIFIED_HANDER_TYPE = "Specified hander is not of type ExecuteResultHandler";
	private static final String ERROR_INSTANTIATE_MESSAGE = "Error instantiate %s.";
	private static final String ERROR_INSTANTIATE = "Error instantiate";
	private static final String TYPE = "type";
	private static final String BUILDIN = "buildin";
	private static final String NO_DEFAULT_CONTRUCTOR_MESSAGE = "No default contructor found for %s.";
	private static final String NO_DEFAULT_CONTRUCTOR = "No default contructor";

	/**
	 * Creates a logger for {@link RunBuildin}.
	 */
	RunBuildinLogger() {
		super(RunBuildin.class);
	}

	CommandException noDefaultCtor(RunBuildin buildin,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(NO_DEFAULT_CONTRUCTOR, e)
				.addContext(BUILDIN, buildin).addContext(TYPE, type),
				NO_DEFAULT_CONTRUCTOR_MESSAGE, type);
	}

	CommandException errorInstantiate(RunBuildin buildin,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(ERROR_INSTANTIATE, e)
				.addContext(BUILDIN, buildin).addContext(TYPE, type),
				ERROR_INSTANTIATE_MESSAGE, type);
	}

	CommandException errorHandlerType(RunBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_HANDER_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_HANDER_TYPE_MESSAGE, object);
	}

	void handlerSet(RunBuildin buildin, Object object) {
		log.debug(SET_HANDLER, object);
	}

	CommandException errorDestroyerType(RunBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_DESTROYER_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_DESTROYER_TYPE_MESSAGE, object);
	}

	void destroyerSet(RunBuildin buildin, Object object) {
		log.debug(SET_DESTROYER, object);
	}

	CommandException errorWatchdogType(RunBuildin buildin, Object object) {
		return logException(new CommandException(SPECIFIED_WATCHDOG_TYPE)
				.addContext(BUILDIN, buildin).addContext(OBJECT, object),
				SPECIFIED_WATCHDOG_TYPE_MESSAGE, object);
	}

	void watchdogSet(RunBuildin buildin, Object object) {
		log.debug(SET_WATCHDOG, object);
	}

	void commandSet(RunBuildin buildin, Object object) {
		log.debug(SET_COMMAND, object);
	}

	void envSet(RunBuildin buildin, Map<String, String> env) {
		log.debug(SET_ENVIRONMENT, env);
	}

}
