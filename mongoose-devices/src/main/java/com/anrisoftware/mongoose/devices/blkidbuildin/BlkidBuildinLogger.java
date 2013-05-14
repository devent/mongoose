package com.anrisoftware.mongoose.devices.blkidbuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

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

	private static final String DEVICE_PATH_SET = "Device path '{}' set for {}.";
	private static final String DEVICE_PATH_NULL = "The device path can not be null.";
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

	void checkDevicePath(BlkidBuildin buildin, File path) {
		notNull(path, DEVICE_PATH_NULL);
	}

	void devicePathSet(BlkidBuildin buildin, File path) {
		if (log.isDebugEnabled()) {
			log.debug(DEVICE_PATH_SET, path, buildin);
		} else {
			log.info(DEVICE_PATH_SET, path, buildin.getTheName());
		}
	}
}
