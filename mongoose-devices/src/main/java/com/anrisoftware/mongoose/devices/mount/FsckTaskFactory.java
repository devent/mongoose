package com.anrisoftware.mongoose.devices.mount;

/**
 * Factory to create {@link FsckTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface FsckTaskFactory {

	/**
	 * Creates the checking task with the device path.
	 * 
	 * @param devicePath
	 *            the device path {@link String}.
	 * 
	 * @return the {@link FsckTask}.
	 */
	FsckTask create(String devicePath);
}
