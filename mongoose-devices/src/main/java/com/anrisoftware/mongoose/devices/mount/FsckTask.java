package com.anrisoftware.mongoose.devices.mount;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
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

	private static final String AUTO_FLAGS = "-a";

	private static final String FORCE_AUTO_FLAGS = "-f -a";

	private static final String EMPTY_FLAGS = "";

	private static final String FORCE_FLAGS = "-f";

	private final FsckTaskLogger log;

	private final String fsckCommand;

	private final String devicePath;

	private final CommandLoader loader;

	private Environment environment;

	private Object outputTarget;

	private Object errorTarget;

	private Object inputSource;

	private Mount mount;

	/**
	 * @see FsckTaskFactory#create(String)
	 */
	@Inject
	FsckTask(FsckTaskLogger logger,
			@Named("mount-properties") ContextProperties p,
			CommandLoader loader, @Assisted String devicePath) {
		this.log = logger;
		this.fsckCommand = p.getProperty("fsck_command");
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
		this.outputTarget = output;
	}

	public void setError(Object error) {
		this.errorTarget = error;
	}

	public void setInput(Object source) {
		this.inputSource = source;
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
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayOutputStream error = new ByteArrayOutputStream();
		Command fsck;
		if (force) {
			fsck = createFsckCommand(output, error, FORCE_AUTO_FLAGS);
		} else {
			fsck = createFsckCommand(output, error, AUTO_FLAGS);
		}
		environment.executeCommandAndWait(fsck);
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
	public void fsck(boolean force) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayOutputStream error = new ByteArrayOutputStream();
		Command fsck;
		if (force) {
			fsck = createFsckCommand(output, error, FORCE_FLAGS);
		} else {
			fsck = createFsckCommand(output, error, EMPTY_FLAGS);
		}
		environment.executeCommandAndWait(fsck);
	}

	private Command createFsckCommand(OutputStream output, OutputStream error,
			String flags) {
		try {
			List<Integer> successExitValues = asList(0, 1, 2, 4);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("successExitValues", successExitValues);
			args.put("terminal", true);

			Command mount = loader.loadCommand("sudo");
			mount.setEnvironment(environment);
			if (outputTarget != null) {
				mount.setOutput(outputTarget);
			}
			if (errorTarget != null) {
				mount.setError(errorTarget);
			}
			if (inputSource != null) {
				mount.setInput(inputSource);
			}
			mount.args(args, format("%s %s %s", fsckCommand, flags, devicePath));
			return mount;
		} catch (Exception e) {
			throw log.errorLoadCommand(mount, e);
		}
	}

}
