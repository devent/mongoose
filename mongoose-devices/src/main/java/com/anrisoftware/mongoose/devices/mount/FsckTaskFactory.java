package com.anrisoftware.mongoose.devices.mount;

/**
 * Factory to create {@link FsckTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface FsckTaskFactory {

	/**
	 * Creates the checking task with the mount-able device.
	 * 
	 * @param mount
	 *            the mount-able {@link Mount} device.
	 * 
	 * @return the {@link FsckTask}.
	 */
	FsckTask create(Mount mount);
}
