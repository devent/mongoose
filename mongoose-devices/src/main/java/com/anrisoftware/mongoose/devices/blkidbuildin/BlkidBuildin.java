package com.anrisoftware.mongoose.devices.blkidbuildin;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

/**
 * Execute the {@code blkid} command on a device path.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BlkidBuildin extends AbstractCommand {

	private static final String BLKID_COMMAND_PROPERTY = "blkid_command";

	private final BlkidBuildinLogger log;

	private final CommandLoader loader;

	private final String blkidCommand;

	private final BlkidParser parser;

	private final Map<String, String> values;

	private Command cmd;

	private File devicePath;

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
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		cmd = loader.createCommand("sudo", getTheEnvironment(), getArgs(),
				output, getError(), getInput(),
				format("%s %s", blkidCommand, devicePath));
		getTheEnvironment().executeCommandAndWait(cmd);
		parser.withDevice(devicePath).withString(output.toString())
				.withValues(values).build();
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, unnamedArgs.size());
		setDevicePath(new File(unnamedArgs.get(0).toString()));
	}

	/**
	 * Sets the device path.
	 * 
	 * @param path
	 *            the device {@link File} path.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 */
	public void setDevicePath(File path) {
		log.checkDevicePath(this, path);
		this.devicePath = path;
		log.devicePathSet(this, path);
	}

	/**
	 * Returns the device path.
	 * 
	 * @return the device {@link File} path.
	 */
	public File getTheDevicePath() {
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
		return values.get("UUID");
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
