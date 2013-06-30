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
package com.anrisoftware.mongoose.buildins.createbuildin;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;

/**
 * The build-in command {@code create}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CreateBuildin extends AbstractCommand {

	private static final String EXTERNAL_COMMAND = "exec";

	private static final String NAME_KEY = "name";

	private final CreateBuildinLogger log;

	private final CommandLoader loader;

	private String name;

	private Command command;

	/**
	 * @param logger
	 *            the {@link CreateBuildinLogger} for logging messages;
	 */
	@Inject
	CreateBuildin(CreateBuildinLogger logger, CommandLoader loader) {
		this.log = logger;
		this.loader = loader;
	}

	@Override
	protected void doCall() throws Exception {
		command = loader.loadCommand(name);
		if (command == null) {
			command = loader.loadCommand(EXTERNAL_COMMAND);
			log.checkRunCommand(this, command);
			insertName(name);
		}
		command.setEnvironment(getTheEnvironment());
		command.args(getArgs(), getUnnamedArgs().toArray());
		log.loadCommand(this, name);
	}

	private void insertName(String name) {
		List<Object> args = getUnnamedArgs();
		args.add(0, name);
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
	 * Returns the name {@code create}.
	 */
	@Override
	public String getTheName() {
		return CreateService.ID;
	}

	/**
	 * Returns the name of the created command.
	 * 
	 * @return the {@link String} command name.
	 */
	public String getTheCommandName() {
		return name;
	}

	/**
	 * Returns the created command.
	 * 
	 * @return the created {@link Command}.
	 */
	public Command getTheCommand() {
		return command;
	}
}
