package com.anrisoftware.mongoose.devices.api;

import java.io.IOException;

/**
 * Device that can be re-sized to a new size.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Resizeable {

	/**
	 * Returns the size of the device.
	 * 
	 * @param unit
	 *            the unit of the size, like bytes, blocks or logical extents.
	 * 
	 * @return the size in units.
	 * 
	 * @throws IOException
	 *             if there was an error get the size of the device.
	 */
	long size(Object unit) throws IOException;

	/**
	 * Resize the device to the new size.
	 * 
	 * @param size
	 *            the new size in units.
	 * 
	 * @param unit
	 *            the unit of the size, like bytes, blocks or logical extents.
	 * 
	 * @throws IOException
	 *             if there was an error resizing the device.
	 */
	void resize(long size, Object unit) throws IOException;
}
