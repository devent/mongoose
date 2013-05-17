package com.anrisoftware.mongoose.devices.mount;

import java.io.File;

/**
 * Factory to create {@link MountTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface MountTaskFactory {

	/**
	 * Creates the mounting task with the device path.
	 * 
	 * @param device
	 *            the device {@link File} path.
	 * 
	 * @return the {@link MountTask}.
	 */
	MountTask create(File device);
}
