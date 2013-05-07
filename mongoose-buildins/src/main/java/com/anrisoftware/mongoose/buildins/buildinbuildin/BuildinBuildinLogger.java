package com.anrisoftware.mongoose.buildins.buildinbuildin;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.commans.Command;

/**
 * Logging messages for {@link BuildinBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BuildinBuildinLogger extends AbstractLogger {

	private static final String COMMAND_NAME_SET = "Command name '{}' set for {}.";
	private static final String COMMAND_NAME_EMPTY = "Command name can not be empty.";
	private static final String COMMAND_NAME = "Command name is expected.";
	private static final String CREATED_COMMAND_INFO = "Created command '{}'.";
	private static final String CREATED_COMMAND = "Created command '{}' for {}.";
	private static final String NO_COMMAND = "No command '%s' found.";

	/**
	 * Create logger for {@link BuildinBuildin}.
	 */
	BuildinBuildinLogger() {
		super(BuildinBuildin.class);
	}

	void checkArguments(BuildinBuildin buildin, boolean containsKey) {
		isTrue(containsKey, COMMAND_NAME);
	}

	void checkName(BuildinBuildin buildin, String name) {
		notBlank(name, COMMAND_NAME_EMPTY);
	}

	void nameSet(BuildinBuildin buildin, String name) {
		if (log.isDebugEnabled()) {
			log.debug(COMMAND_NAME_SET, name, buildin);
		} else {
			log.info(COMMAND_NAME_SET, name, buildin.getTheName());
		}
	}

	void checkCommand(BuildinBuildin buildin, Command command) {
		notNull(command, NO_COMMAND, buildin.getTheCommandName());
	}

	void loadCommand(BuildinBuildin buildin, String name) {
		if (log.isDebugEnabled()) {
			log.debug(CREATED_COMMAND, name, buildin);
		} else {
			log.info(CREATED_COMMAND_INFO, name);
		}
	}
}
