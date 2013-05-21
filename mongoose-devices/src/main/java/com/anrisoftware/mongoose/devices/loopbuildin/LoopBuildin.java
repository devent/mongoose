package com.anrisoftware.mongoose.devices.loopbuildin;

import static com.anrisoftware.globalpom.arrays.ToList.toList;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substring;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.ExecCommand;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Creates loop devices.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LoopBuildin extends AbstractCommand {

	private static final String SUDO_COMMAND = "sudo";

	private static final String SETUP_ARGS = "--find --show";

	private static final String LOSETUP_COMMAND_PROPERTY = "losetup_command";

	private static final String LOSETUP_COMMAND_VARIABLE = "LOSETUP_COMMAND";

	private final LoopBuildinLogger log;

	private final CommandLoader loader;

	private String losetupCommand;

	private File device;

	private File file;

	private boolean created;

	@Inject
	LoopBuildin(LoopBuildinLogger logger,
			@Named("loop-properties") ContextProperties p, CommandLoader loader) {
		this.log = logger;
		this.loader = loader;
		this.losetupCommand = p.getProperty(LOSETUP_COMMAND_PROPERTY);
		this.created = false;
	}

	@Override
	public void setEnvironment(Environment environment) throws CommandException {
		super.setEnvironment(environment);
		Map<String, String> env = environment.getEnv();
		if (env.containsKey(LOSETUP_COMMAND_VARIABLE)) {
			losetupCommand = env.get(LOSETUP_COMMAND_VARIABLE);
		}
	}

	@Override
	protected void doCall() throws Exception {
		if (device != null) {
			infoLoopDevice();
		}
		if (!created) {
			setup();
		}
	}

	private void infoLoopDevice() throws CommandException, Exception {
		String command = format("%s %s", losetupCommand, device);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int[] returnValues = new int[] { 0, 1 };
		ExecCommand cmd = execCommand(command, returnValues, output);
		if (cmd.getTheExitValue() == 1) {
			created = false;
		} else {
			created = true;
		}
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, unnamedArgs.size());
		setFile(new File(unnamedArgs.get(0).toString()));
		if (unnamedArgs.size() > 1) {
			setDevice(new File(unnamedArgs.get(1).toString()));
		}
	}

	/**
	 * Sets the image file.
	 * 
	 * @param file
	 *            the image {@link File}.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the image file.
	 * 
	 * @return the image {@link File}.
	 */
	public File getTheFile() {
		return file;
	}

	/**
	 * Sets the loop device.
	 * 
	 * @param device
	 *            the loop device {@link File} path.
	 */
	public void setDevice(File device) {
		this.device = device;
	}

	/**
	 * Returns the loop device.
	 * 
	 * @return the device {@link File} path.
	 */
	public File getTheDevice() {
		return device;
	}

	@Override
	public String getTheName() {
		return LoopBuildinService.ID;
	}

	/**
	 * Creates the loop device from the image file.
	 * 
	 * @throws CommandException
	 */
	public void setup() throws CommandException {
		log.checkCreated(this, created);
		createLoopDevice();
		created = true;
	}

	private void createLoopDevice() throws CommandException {
		String command;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (device == null) {
			command = format("%s %s %s", losetupCommand, SETUP_ARGS, file);
			execCommand(command, null, output);
			device = new File(substring(output.toString(), 0, -1));
		} else {
			command = format("%s %s %s", losetupCommand, device, file);
			execCommand(command, null, output);
		}
	}

	public void delete() throws Exception {
		log.checkDeleted(this, created);
		deleteLoopDevice();
		device = null;
		created = false;
	}

	private void deleteLoopDevice() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String command = format("%s -d %s", losetupCommand, device);
		execCommand(command, null, output);
	}

	private ExecCommand execCommand(String command, int[] returnValues,
			OutputStream output) throws CommandException {
		Map<String, Object> args = argsMap();
		seccuessExitValues(args, returnValues);
		ExecCommand cmd = (ExecCommand) loader.createCommand(SUDO_COMMAND,
				getTheEnvironment(), args, output, getError(), getInput(),
				command);
		getTheEnvironment().executeCommandAndWait(cmd);
		return cmd;
	}

	private Map<String, Object> argsMap() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.putAll(getArgs());
		return args;
	}

	private void seccuessExitValues(Map<String, Object> args, int[] returnValues) {
		if (returnValues != null) {
			args.put("successExitValues", toList(returnValues));
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("device", device).toString();
	}
}
