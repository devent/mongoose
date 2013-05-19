package com.anrisoftware.mongoose.devices.api;

import java.io.File;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

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
	 * @param name
	 *            the name {@link String} of the device to convert to.
	 * 
	 * @return this {@link Device} converted to the new type.
	 * 
	 * @throws CommandException
	 *             if there were some error converting the device.
	 */
	<T extends Device> T as(String name) throws CommandException;
}
