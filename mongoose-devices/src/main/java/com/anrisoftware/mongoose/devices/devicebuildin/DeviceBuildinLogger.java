package com.anrisoftware.mongoose.devices.devicebuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.net.URI;

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

	private static final String IMAGE_FILE_READ = "Image file '%s' can not be read.";
	private static final String DEVICE_SET = "Set device '{}' to {}.";
	private static final String DEVICE_TYPE = "Device type needs to be set.";
	private static final String DEVICE_NULL = "The device can not be null.";
	private static final String ARGS_NULL = "Needs at least one device path.";
	private static final String FILE_NULL = "Image file can not be null.";
	private static final String FILE_SET = "Image file '{}' set for {}.";

	/**
	 * Create logger for {@link DeviceBuildin}.
	 */
	DeviceBuildinLogger() {
		super(DeviceBuildin.class);
	}

	void checkArgs(DeviceBuildin buildin, int size) {
		isTrue(size > 0, ARGS_NULL);
	}

	void checkDevice(DeviceBuildin buildin, URI device) {
		notNull(device, DEVICE_NULL);
	}

	void checkDeviceScheme(DeviceBuildin buildin, URI device) {
		isTrue(device.getScheme() != null, DEVICE_TYPE);
	}

	void deviceSet(DeviceBuildin buildin, URI device) {
		if (log.isDebugEnabled()) {
			log.debug(DEVICE_SET, device, buildin);
		} else {
			log.debug(DEVICE_SET, device, buildin.getTheName());
		}
	}

	void checkFile(DeviceBuildin buildin, File file) {
		notNull(file, FILE_NULL);
		isTrue(file.canRead(), IMAGE_FILE_READ, file);
	}

	void imageFileSet(DeviceBuildin buildin, File file) {
		if (log.isDebugEnabled()) {
			log.debug(FILE_SET, file, buildin);
		} else {
			log.debug(FILE_SET, file, buildin.getTheName());
		}
	}
}
