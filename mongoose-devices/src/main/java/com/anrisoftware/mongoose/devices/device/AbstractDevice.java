package com.anrisoftware.mongoose.devices.core;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.devices.api.Device;

/**
 * Constructs a device with a name and path.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractDevice implements Device {

	private final String name;

	private final File path;

	/**
	 * Constructs a device with the specified path.
	 * 
	 * @param path
	 *            {@link File} path of the device. The name of the path is used
	 *            as the name of the device.
	 */
	protected AbstractDevice(File path) {
		this(path.getName(), path);
	}

	/**
	 * Constructs a device with the specified name and path.
	 * 
	 * @param name
	 *            the name of the device.
	 * 
	 * @param path
	 *            {@link File} path of the device.
	 */
	protected AbstractDevice(String name, File path) {
		this.name = name;
		this.path = path;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getPath() {
		return path;
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
		return path.equals(rhs.getPath());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append(path).toString();
	}

}
