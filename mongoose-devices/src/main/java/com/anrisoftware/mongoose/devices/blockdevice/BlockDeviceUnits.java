package com.anrisoftware.mongoose.devices.blockdevice;

/**
 * Enumerates the size units of a block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BlockDeviceUnits {

	/**
	 * The size in 512-bytes sectors.
	 */
	SECTORS,

	/**
	 * The block count of the device.
	 */
	BLOCK_COUNT,

	/**
	 * The size of one block of the device in bytes.
	 */
	BLOCK_SIZE,

	/**
	 * The byte size of the device.
	 */
	BYTE_SIZE
}
