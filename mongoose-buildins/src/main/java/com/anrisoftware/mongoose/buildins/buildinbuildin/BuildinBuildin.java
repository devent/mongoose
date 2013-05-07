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
package com.anrisoftware.mongoose.buildins.buildinbuildin;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code buildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BuildinBuildin extends AbstractCommand {

	private static final String NAME_KEY = "name";

	private final BuildinBuildinLogger log;

	private String name;

	private Command command;

	/**
	 * @param logger
	 *            the {@link BuildinBuildinLogger} for logging messages;
	 */
	@Inject
	BuildinBuildin(BuildinBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	protected void doCall() throws CommandException {
		command = loadCommand(name);
		log.checkCommand(this, command);
		command.setEnvironment(getTheEnvironment());
		log.loadCommand(this, name);
		getTheEnvironment().executeCommandAndWait(command);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		parseName(args);
	}

	private void parseName(Map<String, Object> args) {
		log.checkArguments(this, args.containsKey(NAME_KEY));
		setName(args.get(NAME_KEY).toString());
		args.remove(NAME_KEY);
	}

	/**
	 * Sets the command name.
	 * 
	 * @param name
	 *            the command {@link String} name.
	 * 
	 * @throws NullPointerException
	 *             if the specified name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public void setName(String name) {
		log.checkName(this, name);
		this.name = name;
		log.nameSet(this, name);
	}

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the {@link String} name.
	 */
	public String getTheCommandName() {
		return name;
	}

	/**
	 * Returns the executed build-in command.
	 * 
	 * @return the {@link Command}.
	 */
	public Command getTheCommand() {
		return command;
	}

	/**
	 * Returns the name {@code buildin}.
	 */
	@Override
	public String getTheName() {
		return BuildinService.ID;
	}

	/**
	 * Delegates missing methods to the executed command.
	 */
	public Object methodMissing(String name, Object args) {
		return InvokerHelper.invokeMethod(command, name, args);
	}

	/**
	 * Sets property values to the executed command's properties.
	 */
	public void propertyMissing(String name, Object value) {
		InvokerHelper.setProperty(command, name, value);
	}

	/**
	 * Returns property values from the executed command.
	 */
	public Object propertyMissing(String name) {
		return InvokerHelper.getProperty(command, name);
	}

}
