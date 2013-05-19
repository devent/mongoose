package com.anrisoftware.mongoose.devices.devicebuildin;

import groovy.util.Proxy;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Device;
import com.anrisoftware.mongoose.devices.api.DeviceService;
import com.anrisoftware.mongoose.devices.api.DeviceServiceInfo;

public class DeviceBuildin extends AbstractCommand {

	private final DeviceBuildinLogger log;

	private final CommandLoader loader;

	private File path;

	private Device device;

	@Inject
	DeviceBuildin(DeviceBuildinLogger logger, CommandLoader loader) {
		this.log = logger;
		this.loader = loader;
	}

	@Override
	protected void doCall() throws Exception {
		try {
			device = loadDevice(path);
		} catch (CommandException e) {
			if (path.canRead()) {
				createLoDevice(path);
				device = loadDevice(path);
			} else {
				throw e;
			}
		}
	}

	private Device loadDevice(File path) throws Exception {
		Command cmd = executeCommand("blkid", path);
		Proxy proxy = new Proxy();
		proxy.setAdaptee(cmd);
		String type = (String) proxy.getProperty("theType");
		return loadDevice(path, type);
	}

	private Device loadDevice(File path, String type) {
		DeviceServiceInfo info = new DeviceServiceInfo(path, type) {

			@Override
			protected boolean isEquals(DeviceServiceInfo rhs) {
				return false;
			}
		};
		for (DeviceService service : ServiceLoader.load(DeviceService.class)) {
			if (service.getInfo().equals(info)) {
				return service.createDevice(path);
			}
		}
		return null;
	}

	private void createLoDevice(File path) throws Exception {
		Command cmd = executeCommand("lodevice", path);
		Proxy proxy = new Proxy();
		proxy.setAdaptee(cmd);
		this.path = (File) proxy.getProperty("theDevice");
	}

	private Command executeCommand(String name, Object... args)
			throws Exception {
		Command cmd = loader.loadCommand(name);
		cmd.args(getArgs(), args);
		cmd.setEnvironment(getTheEnvironment());
		cmd.setOutput(getOutput());
		cmd.setError(getError());
		getTheEnvironment().executeCommandAndWait(cmd);
		return cmd;
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, unnamedArgs.size());
		setDevice((File) unnamedArgs.get(0));
	}

	/**
	 * Sets the device.
	 * 
	 * @param device
	 *            the device {@link File}.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 */
	public void setDevice(File device) {
		log.checkDevice(this, device);
		this.path = device;
	}

	@Override
	public String getTheName() {
		return "device";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("device", path).toString();
	}
}
