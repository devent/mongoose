package com.anrisoftware.mongoose.devices.mount;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
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

	private final MountTaskLogger log;

	private final String mountCommand;

	private final String umountCommand;

	private final String mountMatchPattern;

	private final Mount device;

	private final CommandLoader loader;

	private Environment environment;

	private Object errorTarget;

	private Object inputSource;

	private Mount mount;

	private Map<String, Object> named;

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
		this.named = new HashMap<String, Object>();
	}

	public void setMount(Mount mount) {
		this.mount = mount;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setOutput(Object output) {
	}

	public void setError(Object error) {
		this.errorTarget = error;
	}

	public void setInput(Object source) {
		this.inputSource = source;
	}

	public void setNamed(Map<String, Object> named) {
		this.named = named;
	}

	public boolean checkMounted(File path) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command mount = createMountCommand("exec", mountCommand, "", output);
		environment.executeCommandAndWait(mount);
		Pattern pattern = compile(format(mountMatchPattern, device, path));
		String mountOutput = output.toString();
		log.mountOutput(this.mount, mountOutput);
		return pattern.matcher(mountOutput).find();
	}

	public void mountDevice(File path) throws CommandException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command mount = createMountCommand("sudo", mountCommand,
				format("%s %s", device, path), output);
		environment.executeCommandAndWait(mount);
	}

	public void umountDevice(File path) throws CommandException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command mount = createMountCommand("sudo", umountCommand,
				path.getAbsolutePath(), output);
		environment.executeCommandAndWait(mount);
	}

	private Command createMountCommand(String command, String name,
			String args, OutputStream output) {
		try {
			Command mount = loader.loadCommand(command);
			mount.setEnvironment(environment);
			mount.setOutput(output);
			if (errorTarget != null) {
				mount.setError(errorTarget);
			}
			if (inputSource != null) {
				mount.setInput(inputSource);
			}
			mount.args(named, format("%s %s", name, args));
			return mount;
		} catch (Exception e) {
			return null;
			// throw log.errorLoadCommand(mount, e);
		}
	}

}
