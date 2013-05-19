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
	 * Mount the device from a specified path.
	 * 
	 * @param path
	 *            the {@link File} path.
	 * 
	 * @throws NullPointerException
	 *             if the specified path is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified path is not a directory; if the device is
	 *             already mounted on the path.
	 * 
	 * @throws IOException
	 *             if there was an error mounting or unmount the device.
	 */
	void mount(File path) throws IOException;

	/**
	 * @see #mount(File)
	 * 
	 * @param mount
	 *            {@code true} if the device should be mounted at the specified
	 *            path; {@code false} if the device should be unmounted from the
	 *            specified path.
	 * 
	 * @throws IllegalArgumentException
	 *             if the device is already mounted on the path; if the device
	 *             is not mounted on the path.
	 */
	void mount(boolean mount, File path) throws IOException;

	/**
	 * Unmount the device from the specified path.
	 * 
	 * @throws IOException
	 *             if there was an error unmount the device.
	 * 
	 * @throws IllegalArgumentException
	 *             if the device is not mounted on the path.
	 */
	void umount(File path) throws IOException;

	/**
	 * Unmount the device from mounted paths.
	 * 
	 * @throws IOException
	 *             if there was an error unmount the device.
	 */
	void umount() throws IOException;

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
	void fsck() throws IOException;

	/**
	 * Check the file system on the device.
	 * 
	 * @param force
	 *            {@code true} to force the check even if the file system is
	 *            clean.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void fsck(boolean force) throws IOException;

	/**
	 * Check the file system on the device and automatically repair it.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void autoFsck() throws IOException;

	/**
	 * Check the file system on the device and automatically repair it.
	 * 
	 * @param force
	 *            {@code true} to force the check even if the file system is
	 *            clean.
	 * 
	 * @throws IOException
	 *             if there was an error checking the file system of the device.
	 */
	void autoFsck(boolean force) throws IOException;

}
