package com.anrisoftware.mongoose.devices.device;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Device;

/**
 * Constructs a device from a path.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractDevice extends AbstractCommand implements Device {

	private File path;

	private AbstractDeviceLogger log;

	private CommandLoader loader;

	@Inject
	void setCommandLoader(CommandLoader loader) {
		this.loader = loader;
	}

	@Inject
	void setAbstractDeviceLogger(AbstractDeviceLogger logger) {
		this.log = logger;
	}

	@Override
	protected void doCall() throws Exception {
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		super.argumentsSet(args, unnamedArgs);
		log.checkArgs(this, unnamedArgs.size());
		setPath((File) unnamedArgs.get(0));
	}

	/**
	 * Sets the device path.
	 * 
	 * @param path
	 *            the device {@link File} path.
	 */
	public void setPath(File path) {
		this.path = path;
		log.devicePathSet(this, path);
	}

	@Override
	public File getThePath() {
		return path;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Device> T as(String name) throws CommandException {
		try {
			Command cmd = loader.createCommand(name, getTheEnvironment(),
					getArgs(), getOutput(), getError(), getInput());
			cmd.args(getUnnamedArgs().toArray());
			getTheEnvironment().executeCommandAndWait(cmd);
			return (T) cmd;
		} catch (Exception e) {
			throw log.errorConvert(this, e, name);
		}
	}

	/**
	 * Equals two devices on their device path.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Device)) {
			return false;
		}
		Device rhs = (Device) obj;
		return path.equals(rhs.getThePath());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(path).toString();
	}

}
