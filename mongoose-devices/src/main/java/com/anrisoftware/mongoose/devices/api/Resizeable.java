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
	 * Returns the block size of the device.
	 * 
	 * @return the block size.
	 */
	long getTheBlockSize();

	/**
	 * Returns the byte size of the device.
	 * 
	 * @return the byte size.
	 */
	long getTheByteSize();

	/**
	 * Resize the device to the new size.
	 * 
	 * @param newSize
	 *            the new size.
	 * 
	 * @param unit
	 *            the {@link BlockUnit} unit.
	 * 
	 * @throws IOException
	 *             if there was an error re-sizing the device.
	 */
	void resize(long newSize, BlockUnit unit) throws IOException;
}
