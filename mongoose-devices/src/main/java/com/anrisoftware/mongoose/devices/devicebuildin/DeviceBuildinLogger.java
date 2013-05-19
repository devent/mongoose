package com.anrisoftware.mongoose.devices.devicebuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link DeviceBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DeviceBuildinLogger extends AbstractLogger {

	private static final String DEVICE_PATH_NULL = "The device path can not be null.";
	private static final String ARGS_NULL = "Need the device path.";

	/**
	 * Create logger for {@link DeviceBuildin}.
	 */
	DeviceBuildinLogger() {
		super(DeviceBuildin.class);
	}

	void checkArgs(DeviceBuildin buildin, int size) {
		isTrue(size > 0, ARGS_NULL);
	}

	void checkDevice(DeviceBuildin buildin, File device) {
		notNull(device, DEVICE_PATH_NULL);
	}
}
