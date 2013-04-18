package com.anrisoftware.linuxdevices.command.factories;

import java.io.File;

import com.anrisoftware.linuxdevices.core.api.Block;

/**
 * Factory to create a new block device from a name or a path.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface BlockDeviceFactory {

	/**
	 * Create a new block device from the specified name and path.
	 * 
	 * @param name
	 *            the name of the device.
	 * 
	 * @param path
	 *            the {@link File} path of the device.
	 * 
	 * @return the {@link Block} block device.
	 */
	Block create(String name, File path);

	/**
	 * Create a new block device from the specified path.
	 * 
	 * @param path
	 *            the {@link File} path of the device.
	 * 
	 * @return the {@link Block} block device.
	 */
	Block create(File path);
}
