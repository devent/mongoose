package com.anrisoftware.mongoose.devices.mount;

/**
 * Factory to create {@link MountTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface MountTaskFactory {

	/**
	 * Creates the mounting task with the mount-able device.
	 * 
	 * @param mount
	 *            the mount-able {@link Mount} device.
	 * 
	 * @return the {@link MountTask}.
	 */
	MountTask create(Mount mount);
}
