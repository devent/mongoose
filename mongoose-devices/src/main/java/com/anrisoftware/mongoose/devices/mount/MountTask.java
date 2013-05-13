package com.anrisoftware.mongoose.devices.mount;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
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

	private final String mountMatchPattern;

	private final String devicePath;

	private final CommandLoader loader;

	private Environment environment;

	private Object errorTarget;

	private Object inputSource;

	private Mount mount;

	/**
	 * @see MountTaskFactory#create(String)
	 */
	@Inject
	MountTask(MountTaskLogger logger,
			@Named("mount-properties") ContextProperties p,
			CommandLoader loader, @Assisted String devicePath) {
		this.log = logger;
		this.mountCommand = p.getProperty("mount_command");
		this.mountMatchPattern = p.getProperty("mount_match_pattern");
		this.loader = loader;
		this.devicePath = devicePath;
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

	public boolean checkMounted(File path) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command mount = createMountCommand(output);
		environment.executeCommandAndWait(mount);
		Pattern pattern = compile(format(mountMatchPattern, devicePath, path));
		String mountOutput = output.toString();
		log.mountOutput(this.mount, mountOutput);
		return pattern.matcher(mountOutput).matches();
	}

	private Command createMountCommand(OutputStream output) {
		try {
			Command mount = loader.loadCommand("exec");
			mount.setEnvironment(environment);
			mount.setOutput(output);
			if (errorTarget != null) {
				mount.setError(errorTarget);
			}
			if (inputSource != null) {
				mount.setInput(inputSource);
			}
			mount.args(format("%s", mountCommand));
			return mount;
		} catch (Exception e) {
			throw log.errorLoadCommand(mount, e);
		}
	}

}
