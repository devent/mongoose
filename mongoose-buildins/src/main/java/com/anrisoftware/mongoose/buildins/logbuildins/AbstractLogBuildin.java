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
package com.anrisoftware.mongoose.buildins.logbuildins;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * Log messages in the specified logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractLogBuildin extends AbstractCommand {

	private AbstractLogBuildinLogger log;

	private String message;

	private Object[] arguments;

	/**
	 * Injects the logger.
	 * 
	 * @param logger
	 *            the {@link AbstractLogBuildinLogger}.
	 */
	@Inject
	void setLogBuildinLogger(AbstractLogBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	protected void doCall() {
		Logger logger = getTheEnvironment().getScriptLogger();
		logMessage(logger);
	}

	/**
	 * Log the message with the arguments in the specified logging level.
	 * 
	 * @param logger
	 *            the {@link Logger}.
	 * 
	 * @see #getMessage()
	 * @see #getArguments()
	 */
	protected abstract void logMessage(Logger logger);

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, unnamedArgs);
		setMessage(unnamedArgs.get(0));
		if (unnamedArgs.size() > 1) {
			setArguments(unnamedArgs);
		}
	}

	private void setMessage(Object object) {
		log.checkMessage(this, object);
		String message = object.toString();
		this.message = message;
		log.messageSet(this, message);
	}

	private void setArguments(List<Object> args) {
		arguments = new Object[args.size() - 1];
		for (int i = 1; i < args.size(); i++) {
			arguments[i - 1] = args.get(i);
		}
		log.argumentsSet(this, arguments);
	}

	/**
	 * Returns the logging message.
	 * 
	 * @return the message.
	 */
	protected String getMessage() {
		return message;
	}

	/**
	 * Returns the arguments for the logging message.
	 * 
	 * @return the arguments.
	 */
	protected Object[] getArguments() {
		return arguments;
	}

	/**
	 * Returns the name of the logger that is used.
	 * 
	 * @return the {@link String} name of the logger
	 */
	public String getTheContext() {
		return getTheEnvironment().getScriptLogger().getName();
	}

	/**
	 * Tests if the logging level is enabled.
	 * 
	 * @return {@code true} if the logging level is enabled or {@code false} if
	 *         not.
	 */
	public abstract boolean getIsEnabled();
}
