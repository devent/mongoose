package com.anrisoftware.mongoose.devices.mount;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Mount the block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MountTask {

	private static final String EXEC_COMMAND = "exec";

	private static final String SUDO_COMMAND = "sudo";

	private final MountTaskLogger log;

	private final String mountCommand;

	private final String umountCommand;

	private final String mountMatchPattern;

	private final Mount device;

	private final CommandLoader loader;

	private final Environment environment;

	private final File devicePath;

	/**
	 * @see MountTaskFactory#create(Mount)
	 */
	@Inject
	MountTask(MountTaskLogger logger,
			@Named("mount-properties") ContextProperties p,
			CommandLoader loader, @Assisted Mount device) {
		this.log = logger;
		this.mountCommand = p.getProperty("mount_command");
		this.umountCommand = p.getProperty("umount_command");
		this.mountMatchPattern = p.getProperty("mount_match_pattern");
		this.loader = loader;
		this.device = device;
		this.devicePath = device.getTheDevice();
		this.environment = device.getTheEnvironment();
	}

	/**
	 * Checks if the specified path is mounted on the device.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @return {@code true} if the path is mounted.
	 * 
	 * @throws CommandException
	 *             if there was an error executing the command.
	 */
	public boolean checkMounted(File path) throws CommandException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command cmd = loader.createCommand(EXEC_COMMAND, environment,
				device.getArgs(), output, device.getError(), device.getInput(),
				mountCommand);
		environment.executeCommandAndWait(cmd);
		Pattern pattern = compile(format(mountMatchPattern, devicePath, path));
		String mountOutput = output.toString();
		log.mountOutput(device, mountOutput);
		return pattern.matcher(mountOutput).find();
	}

	/**
	 * Mount the device on the specified path.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @throws CommandException
	 *             if there was an error executing the command.
	 */
	public void mountDevice(File path) throws CommandException {
		Command cmd = loader.createCommand(SUDO_COMMAND, environment,
				device.getArgs(), device.getOutput(), device.getError(),
				device.getInput(),
				format("%s %s %s", mountCommand, devicePath, path));
		environment.executeCommandAndWait(cmd);
	}

	/**
	 * Unmount the device on the specified path.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @throws CommandException
	 *             if there was an error executing the command.
	 */
	public void umountDevice(File path) throws CommandException {
		Command cmd = loader.createCommand(SUDO_COMMAND, environment,
				device.getArgs(), device.getOutput(), device.getError(),
				device.getInput(), format("%s %s", umountCommand, path));
		environment.executeCommandAndWait(cmd);
	}

}
