package com.anrisoftware.mongoose.devices.api;

import java.io.File;

/**
 * Maps paths to devices.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DevicesMapper {

	/**
	 * Returns the device with the specified path.
	 * 
	 * @param path
	 *            the {@link File} path of the device.
	 * 
	 * @return the {@link Device}.
	 */
	Device getDevice(File path);
}
