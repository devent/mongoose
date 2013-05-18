package com.anrisoftware.mongoose.devices.mount;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link FsckTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MountTaskLogger extends AbstractLogger {

	private static final String MOUNT_OUTPUT = "Mount output: {}.";

	/**
	 * Create logger for {@link FsckTask}.
	 */
	public MountTaskLogger() {
		super(FsckTask.class);
	}

	void mountOutput(Mount mount, String output) {
		log.trace(MOUNT_OUTPUT, output);
	}

}
