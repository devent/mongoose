package com.anrisoftware.mongoose.devices.blkidbuildin;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;

public class BlkidBuildin extends AbstractCommand {

	private static final String BLKID_COMMAND_PROPERTY = "blkid_command";

	private final BlkidBuildinLogger log;

	private final CommandLoader loader;

	private final String blkidCommand;

	private final BlkidParser parser;

	private ByteArrayOutputStream output;

	private ByteArrayOutputStream error;

	private final Map<String, String> values;

	private Command cmd;

	private String devicePath;

	@Inject
	BlkidBuildin(BlkidBuildinLogger logger, BlkidParser parser,
			@Named("blkid-properties") ContextProperties p, CommandLoader loader) {
		this.log = logger;
		this.loader = loader;
		this.parser = parser;
		this.blkidCommand = p.getProperty(BLKID_COMMAND_PROPERTY);
		this.values = new HashMap<String, String>();
	}

	@Override
	protected void doCall() throws Exception {
		output = new ByteArrayOutputStream();
		error = new ByteArrayOutputStream();
		cmd = loader.loadCommand("sudo");
		cmd.setEnvironment(getTheEnvironment());
		cmd.setOutput(output);
		cmd.setError(error);
		cmd.args(getArgs(), format("%s %s", blkidCommand, devicePath));
		getTheEnvironment().executeCommandAndWait(cmd);
		parser.withDevice(devicePath).withString(output.toString())
				.withValues(values).build();
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, unnamedArgs.size());
		setDevicePath(unnamedArgs.get(0).toString());
	}

	/**
	 * Sets the device path.
	 * 
	 * @param path
	 *            the device {@link String} path.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified path is empty.
	 */
	public void setDevicePath(String path) {
		log.checkDevicePath(this, path);
		this.devicePath = path;
	}

	/**
	 * Returns the device path.
	 * 
	 * @return the device {@link String} path.
	 */
	public String getTheDevicePath() {
		return devicePath;
	}

	/**
	 * Returns the secondary type of the device.
	 * 
	 * @return the device {@link String} secondary type, or {@code null}.
	 */
	public String getTheSecondaryType() {
		return values.get("SEC_TYPE");
	}

	/**
	 * Returns the type of the device.
	 * 
	 * @return the device {@link String} type, or {@code null}.
	 */
	public String getTheType() {
		return values.get("TYPE");
	}

	/**
	 * Returns the UUID of the device.
	 * 
	 * @return the device {@link String} UUID, or {@code null}.
	 */
	public String getTheUUID() {
		return values.get("TYPE");
	}

	/**
	 * Returns the label of the device.
	 * 
	 * @return the device {@link String} label, or {@code null}.
	 */
	public String getTheLabel() {
		return values.get("LABEL");
	}

	@Override
	public String getTheName() {
		return "blkid";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("device path", devicePath).append(values).toString();
	}
}
