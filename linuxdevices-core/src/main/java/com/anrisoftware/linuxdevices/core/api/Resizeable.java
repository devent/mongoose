package com.anrisoftware.linuxdevices.core.api;

import java.io.IOException;

/**
 * Resizebale devices. The devices can be re-sized to a new size.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Resizeable {

	/**
	 * Returns the block size of the device.
	 * 
	 * @return the block size.
	 */
	long getBlockSize();

	/**
	 * Returns the byte size of the device.
	 * 
	 * @return the byte size.
	 */
	long getByteSize();

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