package com.anrisoftware.mongoose.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Loads a command as a Java service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class CommandLoader {

	private final CommandLoaderLogger log;

	@Inject
	public CommandLoader(CommandLoaderLogger logger) {
		this.log = logger;
	}

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

	/**
	 * Loads the commands and setups the output targets and input source.
	 * 
	 * @param command
	 *            the command {@link String} name.
	 * 
	 * @param environment
	 *            the {@link Environment} of the command.
	 * 
	 * @param named
	 *            the named parameter {@link Map} of the command.
	 * 
	 * @param outputTarget
	 *            the standard output target {@link OutputStream} stream, can be
	 *            {@code null}.
	 * 
	 * @param errorTarget
	 *            the standard error target {@link OutputStream} stream, can be
	 *            {@code null}.
	 * 
	 * @param inputSource
	 *            the standard input source {@link InputStream} stream, can be
	 *            {@code null}.
	 * 
	 * @param args
	 *            the arguments for the command.
	 * 
	 * @return the loaded {@link Command}.
	 * 
	 * @throws CommandException
	 *             if there was an error loading the command.
	 */
	public Command createCommand(String command, Environment environment,
			Map<String, Object> named, OutputStream outputTarget,
			OutputStream errorTarget, InputStream inputSource, Object... args)
			throws CommandException {
		try {
			Command cmd = loadCommand(command);
			log.checkCommand(cmd, command);
			cmd.setEnvironment(environment);
			setupStreams(cmd, outputTarget, errorTarget, inputSource);
			cmd.args(named, args);
			return cmd;
		} catch (Exception e) {
			throw log.errorLoadCommand(e, command);
		}
	}

	private void setupStreams(Command mount, OutputStream outputTarget,
			OutputStream errorTarget, InputStream inputSource) throws Exception {
		if (outputTarget != null) {
			mount.setOutput(outputTarget);
		}
		if (errorTarget != null) {
			mount.setError(errorTarget);
		}
		if (inputSource != null) {
			mount.setInput(inputSource);
		}
	}

}
