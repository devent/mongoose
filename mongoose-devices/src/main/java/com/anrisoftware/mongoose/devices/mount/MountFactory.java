package com.anrisoftware.mongoose.devices.mount;

/**
 * Factory to create the mount utility.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface MountFactory {

	/**
	 * Creates the mount utility with the specified device path.
	 * 
	 * @param devicePath
	 *            the device {@link String} path.
	 * 
	 * @return the {@link Mount}.
	 */
	Mount create(String devicePath);
}
