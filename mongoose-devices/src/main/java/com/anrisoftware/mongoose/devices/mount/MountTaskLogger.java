package com.anrisoftware.mongoose.devices.mount;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link MountTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MountTaskLogger extends AbstractLogger {

	private static final String MOUNT_OUTPUT = "Mount output: ``{}''";

	/**
	 * Create logger for {@link MountTask}.
	 */
	public MountTaskLogger() {
		super(MountTask.class);
	}

	void mountOutput(Mount mount, String output) {
		log.trace(MOUNT_OUTPUT, output);
	}

}
