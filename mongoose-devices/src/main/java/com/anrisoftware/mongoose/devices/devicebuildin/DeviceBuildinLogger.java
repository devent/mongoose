package com.anrisoftware.mongoose.devices.devicebuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.net.URI;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * Logging messages for {@link DeviceBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DeviceBuildinLogger extends AbstractLogger {

	private static final String DEVICE_SET = "Set device '{}' to {}.";
	private static final String DEVICE_TYPE = "Device type needs to be set for %s.";
	private static final String DEVICE_NULL = "Device can not be null.";
	private static final String DEVICE_PATH_NULL = "Device path can not be null for %s.";
	private static final String ARGS_NULL = "Needs at least one device path.";
	private static final String DEVICE_PATH_SET = "Set device path '{}' to {}.";
	private static final String TYPE_SET = "Device type '{}' set for {}.";
	private static final String LOOP_PATH_NULL = "Loop device path can not be null or empty for %s.";
	private static final String LOOP_PATH_SET = "Loop device '{}' set for {}.";

	/**
	 * Create logger for {@link DeviceBuildin}.
	 */
	DeviceBuildinLogger() {
		super(DeviceBuildin.class);
	}

	void checkType(DeviceBuildin buildin, String scheme) {
		notBlank(scheme, DEVICE_TYPE, buildin);
	}

	void typeSet(DeviceBuildin buildin, String type) {
		if (log.isDebugEnabled()) {
			log.debug(TYPE_SET, type, buildin);
		} else {
			log.debug(TYPE_SET, type, buildin.getTheName());
		}
	}

	void checkArgs(AbstractCommand buildin, int size) {
		isTrue(size > 0, ARGS_NULL);
	}

	void checkDevice(AbstractCommand buildin, URI device) {
		notNull(device, DEVICE_NULL);
	}

	void deviceSet(DeviceBuildin buildin, File device) {
		if (log.isDebugEnabled()) {
			log.debug(DEVICE_SET, device, buildin);
		} else {
			log.debug(DEVICE_SET, device, buildin.getTheName());
		}
	}

	void checkDevicePath(DeviceBuildin buildin, Object path) {
		notNull(path, DEVICE_PATH_NULL, buildin);
	}

	void devicePathSet(DeviceBuildin buildin, File device) {
		if (log.isDebugEnabled()) {
			log.debug(DEVICE_PATH_SET, device, buildin);
		} else {
			log.debug(DEVICE_PATH_SET, device, buildin.getTheName());
		}
	}

	void checkLoopPath(DeviceBuildin buildin, String path) {
		notBlank(path, LOOP_PATH_NULL, buildin);
	}

	void loopPathSet(DeviceBuildin buildin, String path) {
		if (log.isDebugEnabled()) {
			log.debug(LOOP_PATH_SET, path, buildin);
		} else {
			log.debug(LOOP_PATH_SET, path, buildin.getTheName());
		}
	}

}
