package com.anrisoftware.mongoose.devices.mount;

import java.io.File;

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
	 * @param path
	 *            the device {@link File} path.
	 * 
	 * @return the {@link Mount}.
	 */
	Mount create(File path);
}
