package com.anrisoftware.mongoose.devices.api;

import java.io.File;

/**
 * The Linux device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Device {

	/**
	 * Returns the name of the device. The name is the identification of the
	 * device, i.e. {@code sda1}, {@code md0}, {@code lv_volume}.
	 * 
	 * @return the name of the device.
	 */
	String getName();

	/**
	 * Returns the path of the device.
	 * 
	 * @return the {@link File} path.
	 */
	File getPath();
}
