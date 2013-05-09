package com.anrisoftware.mongoose.command;

import java.util.ServiceLoader;

import javax.inject.Singleton;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;

/**
 * Loads a command as a Java service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class CommandLoader {

	/**
	 * Loads the command with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the command.
	 * 
	 * @return the {@link Command} or {@code null} if no such command can be
	 *         found.
	 */
	public Command loadCommand(String name) {
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

}
