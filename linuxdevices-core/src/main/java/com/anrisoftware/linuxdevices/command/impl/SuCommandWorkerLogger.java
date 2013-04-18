package com.anrisoftware.linuxdevices.command.impl;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link SuCommandWorkerImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class SuCommandWorkerLogger extends AbstractLogger {

	/**
	 * Creates the logger for {@link SuCommandWorkerImpl}.
	 */
	SuCommandWorkerLogger() {
		super(SuCommandWorkerImpl.class);
	}

	void userIsNotRoot(String command, String sudoCommand) {
		log.trace("User is not root, executing command ``{}'' with ``{}''.",
				command, sudoCommand);
	}

}
