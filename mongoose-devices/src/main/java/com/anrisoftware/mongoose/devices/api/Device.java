package com.anrisoftware.mongoose.devices.api;

import java.io.File;

import com.anrisoftware.mongoose.api.commans.Command;

/**
 * The Linux device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Device extends Command {

	/**
	 * Returns the path of the device.
	 * 
	 * @return the {@link File} path.
	 */
	File getThePath();

	/**
	 * Converts to a different type of device.
	 * 
	 * @param type
	 *            the {@link Class} type of the device to convert to.
	 * 
	 * @return this {@link Device} converted to the new type.
	 */
	<T extends Device> T asType(Class<T> type);
}
