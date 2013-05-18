package com.anrisoftware.mongoose.devices.device;

import static org.apache.commons.lang3.Validate.isTrue;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractDevice}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AbstractDeviceLogger extends AbstractLogger {

	private static final String SET_DEVICE_PATH = "Set device path {} for {}.";
	private static final String DEVICE_PATH = "Need the device path.";

	/**
	 * Create logger for {@link AbstractDevice}.
	 */
	public AbstractDeviceLogger() {
		super(AbstractDevice.class);
	}

	void checkArgs(AbstractDevice device, int size) {
		isTrue(size > 0, DEVICE_PATH);
	}

	void devicePathSet(AbstractDevice device, File path) {
		if (log.isDebugEnabled()) {
			log.debug(SET_DEVICE_PATH, path, device);
		} else {
			log.info(SET_DEVICE_PATH, path, device.getTheName());
		}
	}
}
