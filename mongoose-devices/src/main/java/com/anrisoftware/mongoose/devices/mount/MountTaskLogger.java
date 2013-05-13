package com.anrisoftware.mongoose.devices.mount;

import javax.inject.Singleton;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

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
	private static final String ERROR_LOAD_COMMAND = "Error load command";

	/**
	 * Create logger for {@link FsckTask}.
	 */
	public MountTaskLogger() {
		super(FsckTask.class);
	}

	RuntimeException errorLoadCommand(Mount mount, Exception e) {
		return logException(
				new ContextedRuntimeException(ERROR_LOAD_COMMAND, e),
				ERROR_LOAD_COMMAND);
	}

	void mountOutput(Mount mount, String output) {
		log.trace(MOUNT_OUTPUT, output);
	}

}
