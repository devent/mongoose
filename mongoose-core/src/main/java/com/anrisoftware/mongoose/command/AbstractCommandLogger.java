package com.anrisoftware.mongoose.command;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.ObjectUtils;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractCommand}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractCommandLogger extends AbstractLogger {

	/**
	 * Create logger for {@link AbstractCommand}.
	 */
	public AbstractCommandLogger() {
		super(AbstractCommand.class);
	}

	void checkArgs(AbstractCommand command, Object args) {
		notNull(args, "The arguments cannot be null.");
	}

	void argumentsSet(AbstractCommand command, Object args) {
		log.debug("Set arguments {} for {}.", args, command);
	}

	void checkSource(AbstractCommand command, Object source) {
		notNull(source, "The source cannot be null.");
	}

	void checkTarget(AbstractCommand command, Object target) {
		notNull(target, "The target cannot be null.");
	}

	void sourceSet(AbstractCommand command, Object source) {
		log.debug("Set source {} for {}.", source, command);
	}

	void outputTargetSet(AbstractCommand command, Object target) {
		if (target instanceof ByteArrayOutputStream) {
			target = ObjectUtils.identityToString(target);
		}
		log.debug("Set output target {} for {}.", target, command);
	}

	void errorTargetSet(AbstractCommand command, Object target) {
		log.debug("Set error target {} for {}.", target, command);
	}
}
