package com.anrisoftware.mongoose.devices.api;

/**
 * Block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Block extends Device, Mountable, Resizeable {

	/**
	 * Returns the unique name depending on the hardware serial number.
	 * 
	 * @return the ID of the device.
	 */
	String getTheId();

	/**
	 * Returns the label of the device.
	 * 
	 * @return the label of the device.
	 */
	String getTheLabel();

	/**
	 * Returns the unique identifier of the device.
	 * 
	 * @return the UUID of the device.
	 */
	String getTheUUID();
}
