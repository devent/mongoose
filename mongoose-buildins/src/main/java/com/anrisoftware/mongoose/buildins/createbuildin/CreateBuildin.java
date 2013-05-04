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
import java.util.ServiceLoader;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.anrisoftware.mongoose.api.exceptions.ExecutionException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code create}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CreateBuildin extends AbstractCommand {

	private final CreateBuildinLogger log;

	private String name;

	private Command command;

	/**
	 * @param logger
	 *            the {@link CreateBuildinLogger} for logging messages;
	 */
	@Inject
	CreateBuildin(CreateBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	protected void doCall() throws ExecutionException {
		command = loadCommand(name);
		log.checkCommand(this, command);
		command.setEnvironment(getTheEnvironment());
		log.loadCommand(this, name);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArguments(this, unnamedArgs.size());
		if (unnamedArgs.size() == 1) {
			name = unnamedArgs.get(0).toString();
		}
	}

	private Command loadCommand(String name) {
		CommandService service = loadCommandService(name);
		return service == null ? null : service.getCommandFactory().create();
	}

	private CommandService loadCommandService(String name) {
		for (CommandService service : ServiceLoader.load(CommandService.class)) {
			if (service.getInfo().getId().equals(name)) {
				return service;
			}
		}
		return null;
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
