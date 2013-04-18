package com.anrisoftware.linuxdevices.block;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BlockDeviceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class BlockDeviceImplLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link BlockDeviceImpl}.
	 */
	BlockDeviceImplLogger() {
		super(BlockDeviceImpl.class);
	}

	void checkMountPath(BlockDeviceImpl device, File path) {
		notNull(path, "The mount path for the device %s cannot be null.",
				device);
	}

	IOException errorMountDevice(BlockDeviceImpl device, File path, int code,
			String error) {
		IOException ex = new IOException(
				format("Could not mount the device %s at the path %s. Mount command returns %d: %s",
						device, path, code, error));
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	IOException errorCheckMounted(BlockDeviceImpl device, File path, int code,
			String error) {
		IOException ex = new IOException(
				format("Could not check if the device %s is mounted at the path %s. Mount command returns %d: %s",
						device, path, code, error));
		log.error(ex.getLocalizedMessage());
		return ex;
	}

}
