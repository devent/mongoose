package com.anrisoftware.mongoose.devices.mount;

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
	 * @param devicePath
	 *            the device path {@link String}.
	 * 
	 * @return the {@link MountTask}.
	 */
	MountTask create(String devicePath);
}
