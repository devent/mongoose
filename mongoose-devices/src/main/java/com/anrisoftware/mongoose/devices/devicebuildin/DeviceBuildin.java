package com.anrisoftware.mongoose.devices.devicebuildin;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;

/**
 * Maps a device path to a device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DeviceBuildin extends AbstractCommand {

	private static final String TYPE_KEY = "type";

	private static final String LODEVICE_COMMAND = "lodevice";

	private static final String FILE_TYPE = "file";

	private final DeviceBuildinLogger log;

	private final CommandLoader loader;

	private File loopImage;

	private List<Object> deviceArgs;

	private String type;

	private Command device;

	@Inject
	DeviceBuildin(DeviceBuildinLogger logger, CommandLoader loader) {
		this.log = logger;
		this.loader = loader;
		this.device = null;
	}

	@Override
	protected void doCall() throws Exception {
		Command cmd;
		if (loopImage != null) {
			cmd = createLoDevice();
		} else {
			cmd = createDevice(type);
		}
		device = cmd;
		getTheEnvironment().executeCommandAndWait(cmd);
	}

	private Command createDevice(String type) throws CommandException {
		return loader.createCommand(type, getTheEnvironment(), getArgs(),
				getOutput(), getError(), getInput(), deviceArgs);
	}

	private Command createLoDevice() throws Exception {
		return loader.createCommand(LODEVICE_COMMAND, getTheEnvironment(),
				getArgs(), getOutput(), getError(), getInput(), deviceArgs);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		if (args.containsKey(TYPE_KEY)) {
			type = args.get(TYPE_KEY).toString();
		}
		log.checkArgs(this, unnamedArgs.size());
		this.deviceArgs = new ArrayList<Object>(unnamedArgs);
		setupUnnamed(unnamedArgs);
	}

	private void setupUnnamed(List<Object> args) {
		if (args.get(0) instanceof URI) {
			setDeviceURI((URI) args.get(0));
		} else if (args.get(0) instanceof File) {
			setImageFile((File) args.get(0));
		} else {
			argURIOrFile(args.get(0));
		}
	}

	private void argURIOrFile(Object arg) {
		URI uri = asUri(arg.toString());
		if (uri != null && !isFileType(uri)) {
			setDeviceURI(uri);
		} else if (uri != null && isFileType(uri)) {
			setImageFile(new File(uri.getPath()));
		} else {
			setImageFile(new File(arg.toString()));
		}
	}

	private boolean isFileType(URI uri) {
		return FILE_TYPE.equals(uri.getScheme());
	}

	private URI asUri(String string) {
		try {
			return new URI(string);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Sets the image file.
	 * 
	 * @param file
	 *            the image {@link File} of the device; a loop device is created
	 *            for the image file.
	 * 
	 * @throws NullPointerException
	 *             if the specified device is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the image file can not be read.
	 */
	public void setImageFile(File file) {
		log.checkFile(this, file);
		this.loopImage = file;
		deviceArgs.set(0, file);
		log.imageFileSet(this, file);
	}

	/**
	 * Sets the device.
	 * 
	 * @param device
	 *            the {@link URI} of the device; if no device type was set the
	 *            URI's scheme is used.
	 * 
	 * @throws NullPointerException
	 *             if the specified device is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the device URI does not have a scheme and the type was not
	 *             set.
	 */
	public void setDeviceURI(URI device) {
		log.checkDevice(this, device);
		if (type == null) {
			log.checkDeviceScheme(this, device);
			type = device.getScheme();
		}
		deviceArgs.set(0, device.getPath());
		log.deviceSet(this, device);
	}

	/**
	 * Returns the device.
	 * 
	 * @return the device {@link Command}.
	 */
	public Command getTheDevice() {
		return device;
	}

	@Override
	public String getTheName() {
		return DeviceBuildinService.ID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("device", device).toString();
	}
}
