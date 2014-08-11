package com.anrisoftware.mongoose.command;

import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link CommandLoader}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CommandLoaderLogger extends AbstractLogger {

	private static final String COMMAND_NULL = "Could not find command '%s',";
	private static final String ERROR_LOAD_COMMAND = "Error load command";
	private static final String ERROR_LOAD_COMMAND_MESSAGE = "Error load command '%s'.";

	/**
	 * Create logger for {@link CommandLoader}.
	 */
	public CommandLoaderLogger() {
		super(CommandLoader.class);
	}

	CommandException errorLoadCommand(Exception e, String name) {
		return logException(
				new CommandException(ERROR_LOAD_COMMAND, e).add("name",
						name), ERROR_LOAD_COMMAND_MESSAGE, name);
	}

	void checkCommand(Command cmd, String command) {
		notNull(cmd, COMMAND_NULL, command);
	}

}
