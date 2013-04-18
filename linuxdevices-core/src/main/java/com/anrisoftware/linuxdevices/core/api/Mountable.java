package com.anrisoftware.linuxdevices.core.api;

import java.io.File;
import java.io.IOException;

/**
 * Mountable devices are devices that can be mounted to a directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
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
	void setMounted(boolean mount, File path) throws IOException;

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
	boolean isMounted(File path) throws IOException;

	/**
	 * Check the file system on the device.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void checkFilesystem() throws IOException;

	/**
	 * Check the file system on the device.
	 * 
	 * @param forceIfMounted
	 *            because some file systems should not be checked if they are
	 *            mounted the operation will fail for those file systems.
	 *            Specifying {@code true} to force the check even if such a
	 *            system is mounted.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void checkFilesystem(boolean forceIfMounted) throws IOException;

}
