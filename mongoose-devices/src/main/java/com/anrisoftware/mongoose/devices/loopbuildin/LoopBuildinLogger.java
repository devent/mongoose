package com.anrisoftware.mongoose.devices.loopbuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link LoopBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class LoopBuildinLogger extends AbstractLogger {

	private static final String BUILDIN = "buildin";
	private static final String DEVICE_ALREADY_CREATED_MESSAGE = "Loop device '%s' already created";
	private static final String DEVICE_ALREADY_CREATED = "Loop device already created";
	private static final String DEVICE_PATH_SET = "Device path '{}' set for {}.";
	private static final String DEVICE_PATH_NULL = "The device path can not be null.";
	private static final String ARGS_NULL = "Need the device path.";
	private static final String DEVICE_ALREADY_DELETED = "Loop device already deleted";
	private static final String DEVICE_ALREADY_DELETED_MESSAGE = "Loop device '%s' already deleted";

	/**
	 * Create logger for {@link LoopBuildin}.
	 */
	LoopBuildinLogger() {
		super(LoopBuildin.class);
	}

	void checkArgs(LoopBuildin buildin, int size) {
		isTrue(size > 0, ARGS_NULL);
	}

	void checkDevicePath(LoopBuildin buildin, File path) {
		notNull(path, DEVICE_PATH_NULL);
	}

	void devicePathSet(LoopBuildin buildin, File path) {
		if (log.isDebugEnabled()) {
			log.debug(DEVICE_PATH_SET, path, buildin);
		} else {
			log.info(DEVICE_PATH_SET, path, buildin.getTheName());
		}
	}

	void checkCreated(LoopBuildin buildin, boolean created)
			throws CommandException {
		if (created) {
			throw logException(
					new CommandException(DEVICE_ALREADY_CREATED).addContext(
							BUILDIN, buildin), DEVICE_ALREADY_CREATED_MESSAGE,
					buildin.getTheDevice());
		}
	}

	void checkDeleted(LoopBuildin buildin, boolean created)
			throws CommandException {
		if (!created) {
			throw logException(
					new CommandException(DEVICE_ALREADY_DELETED).addContext(
							BUILDIN, buildin), DEVICE_ALREADY_DELETED_MESSAGE,
					buildin.getTheDevice());
		}
	}
}
