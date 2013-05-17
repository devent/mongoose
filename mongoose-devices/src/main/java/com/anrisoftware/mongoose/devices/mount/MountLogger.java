package com.anrisoftware.mongoose.devices.mount;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Mount}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MountLogger extends AbstractLogger {

	private static final String DEVICE_MOUNTED = "Device %s already mounted on %s.";
	private static final String PATH_DIRECTORY = "Path '%s' must be directory.";
	private static final String PATH_NULL = "The path can not be null.";
	private static final String DEVICE_NOT_MOUNTED = "Device %s is not mounted on %s.";

	/**
	 * Create logger for {@link Mount}.
	 */
	public MountLogger() {
		super(Mount.class);
	}

	void checkPath(Mount mount, File path) {
		notNull(path, PATH_NULL);
		isTrue(path.isDirectory(), PATH_DIRECTORY, path);
	}

	void checkNotMounted(Mount mount, boolean mounted, File path) {
		isTrue(!mounted, DEVICE_MOUNTED, mount.getDevice(), path);
	}

	void checkMounted(Mount mount, boolean mounted, File path) {
		isTrue(mounted, DEVICE_NOT_MOUNTED, mount.getDevice(), path);
	}

	void mountDevice(Mount mount, File path) {
		if (log.isDebugEnabled()) {
			log.debug("Mount {} on '{}'.", mount, path);
		} else {
			log.info("Mount device '{}' on '{}'.", mount.getDevice(), path);
		}
	}
}
