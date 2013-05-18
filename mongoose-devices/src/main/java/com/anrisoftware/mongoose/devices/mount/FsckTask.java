package com.anrisoftware.mongoose.devices.mount;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Check the file system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FsckTask {

	private static final String FSCK_COMMAND_PROPERTY = "fsck_command";

	private static final String FSCK_COMMAND_VARIABLE = "FSCK_COMMAND";

	private static final String SUDO_COMMAND = "sudo";

	private static final String AUTO_FLAGS = "-a";

	private static final String FORCE_AUTO_FLAGS = "-f -a";

	private static final String FORCE_FLAGS = "-f";

	private final String fsckCommand;

	private final CommandLoader loader;

	private final File path;

	private final Environment environment;

	private final Mount device;

	/**
	 * @see FsckTaskFactory#create(Mount)
	 */
	@Inject
	FsckTask(@Named("mount-properties") ContextProperties p,
			CommandLoader loader, @Assisted Mount device) {
		this.loader = loader;
		this.device = device;
		this.path = device.getThePath();
		this.environment = device.getTheEnvironment();
		this.fsckCommand = fsckCommand(p, environment);
	}

	private String fsckCommand(ContextProperties p, Environment environment) {
		Map<String, String> env = environment.getEnv();
		if (env.containsKey(FSCK_COMMAND_VARIABLE)) {
			return env.get(FSCK_COMMAND_VARIABLE);
		} else {
			return p.getProperty(FSCK_COMMAND_PROPERTY);
		}
	}

	/**
	 * @see Mount#autoFsck()
	 */
	public void autoFsck() throws IOException {
		autoFsck(false);
	}

	/**
	 * @see Mount#autoFsck(boolean)
	 */
	public void autoFsck(boolean force) throws IOException {
		String command;
		if (force) {
			command = format("%s %s %s", fsckCommand, FORCE_AUTO_FLAGS, path);
		} else {
			command = format("%s %s %s", fsckCommand, AUTO_FLAGS, path);
		}
		environment.executeCommandAndWait(createFsckCommand(command));
	}

	/**
	 * @see Mount#fsck()
	 */
	public void fsck() throws IOException {
		fsck(false);
	}

	/**
	 * @see Mount#fsck(boolean)
	 */
	public void fsck(boolean force) throws CommandException {
		String command;
		if (force) {
			command = format("%s %s %s", fsckCommand, FORCE_FLAGS, path);
		} else {
			command = format("%s %s", fsckCommand, path);
		}
		environment.executeCommandAndWait(createFsckCommand(command));
	}

	private Command createFsckCommand(String command) throws CommandException {
		List<Integer> successExitValues = asList(0, 1, 2, 4);
		Map<String, Object> args = new HashMap<String, Object>(device.getArgs());
		args.put("successExitValues", successExitValues);
		args.put("terminal", true);
		return loader.createCommand(SUDO_COMMAND, environment, args,
				device.getOutput(), device.getError(), device.getInput(),
				command);
	}
}
