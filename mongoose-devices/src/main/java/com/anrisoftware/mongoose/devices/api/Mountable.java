package com.anrisoftware.mongoose.devices.api;

import java.io.File;
import java.io.IOException;

/**
 * Device that can be mounted to a directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Mountable {

	/**
	 * Mount or un-mount the device from a specified path.
	 * 
	 * @param mount
	 *            {@code true} if the device should be mounted at the specified
	 *            path. {@code false} if the device should be un-mounted from
	 *            the specified path.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 * 
	 * @throws IOException
	 *             if there was an error mounting or un-mounting the device.
	 */
	void mount(boolean mount, File path) throws IOException;

	/**
	 * Un-mount the device from a specified path.
	 * 
	 * @param mount
	 *            {@code false} if the device should be un-mounted from the
	 *            specified path.
	 * 
	 * @throws IOException
	 *             if there was an error un-mounting the device.
	 * 
	 * @throws NullPointerException
	 *             if the device should be mounted.
	 */
	void mount(boolean mount) throws IOException;

	/**
	 * Returns if the device is mounted on the specified path.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @return {@code true} if the device is mounted on the specified path or
	 *         {@code false} if not.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 * 
	 * @throws IOException
	 *             if there was an error checking if the device is mounted.
	 */
	boolean getIsMounted(File path) throws IOException;

	/**
	 * Check the file system on the device.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void fsck() throws IOException;

	/**
	 * Check the file system on the device.
	 * 
	 * @param force
	 *            because some file systems should not be checked if they are
	 *            mounted the operation will fail for those file systems.
	 *            Specifying {@code true} to force the check even if such a
	 *            system is mounted.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void fsck(boolean force) throws IOException;

}
