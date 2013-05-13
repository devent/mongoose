package com.anrisoftware.mongoose.devices.blkidbuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BlkidBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BlkidBuildinLogger extends AbstractLogger {

	private static final String DEVICE_PATH_NULL = "The device path can not be null or empty.";
	private static final String ARGS_NULL = "Need the device path.";

	/**
	 * Create logger for {@link BlkidBuildin}.
	 */
	BlkidBuildinLogger() {
		super(BlkidBuildin.class);
	}

	void checkArgs(BlkidBuildin buildin, int size) {
		isTrue(size > 0, ARGS_NULL);
	}

	void checkDevicePath(BlkidBuildin buildin, String path) {
		notBlank(path, DEVICE_PATH_NULL);
	}
}
