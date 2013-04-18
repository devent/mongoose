package com.anrisoftware.linuxdevices.core.api;

/**
 * Block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Block extends Device, Mountable, Resizeable {

	/**
	 * Returns the unique name depending on the hardware serial number.
	 * 
	 * @return the ID of the device.
	 */
	String getId();

	/**
	 * Returns the label of the device.
	 * 
	 * @return the label of the device.
	 */
	String getLabel();

	/**
	 * Returns the unique identifier of the device.
	 * 
	 * @return the UUID of the device.
	 */
	String getUuid();
}
