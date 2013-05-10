package com.anrisoftware.mongoose.command;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.ObjectUtils;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link AbstractCommand}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractCommandLogger extends AbstractLogger {

	private static final String NAME = "name";
	private static final String SET_PIPE_BUFFER_SIZE_INFO = "Set the pipe buffer size to {} for command {}.";
	private static final String SET_PIPE_BUFFER_SIZE = "Set the pipe buffer size to {} for {}.";
	private static final String PIPE_BUFFER_SIZE = "Pipe buffer size not be zero or negative.";
	private static final String ERROR_INSTANTIATE_MESSAGE = "Error instantiate %s.";
	private static final String ERROR_INSTANTIATE = "Error instantiate";
	private static final String TYPE = "type";
	private static final String COMMAND = "command";
	private static final String NO_DEFAULT_CONTRUCTOR_MESSAGE = "No default contructor found for %s.";
	private static final String NO_DEFAULT_CONTRUCTOR = "No default contructor";
	private static final String ERROR_CLASS_NOT_FOUNT = "Type not found in class path";
	private static final String ERROR_CLASS_NOT_FOUNT_MESSAGE = "Type '%s' not found in class path";

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

	void checkPipeBuffer(AbstractCommand command, int size) {
		isTrue(size > 0, PIPE_BUFFER_SIZE);
	}

	void pipeBufferSizeSet(AbstractCommand command, int size) {
		if (log.isDebugEnabled()) {
			log.debug(SET_PIPE_BUFFER_SIZE, size, command);
		} else {
			log.info(SET_PIPE_BUFFER_SIZE_INFO, size, command.getTheName());
		}
	}

	CommandException noDefaultCtor(AbstractCommand command,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(NO_DEFAULT_CONTRUCTOR, e)
				.addContext(COMMAND, command).addContext(TYPE, type),
				NO_DEFAULT_CONTRUCTOR_MESSAGE, type);
	}

	CommandException errorInstantiate(AbstractCommand command,
			ReflectiveOperationException e, Class<?> type) {
		return logException(new CommandException(ERROR_INSTANTIATE, e)
				.addContext(COMMAND, command).addContext(TYPE, type),
				ERROR_INSTANTIATE_MESSAGE, type);
	}

	CommandException classNotFound(AbstractCommand command,
			ClassNotFoundException e, String name) {
		return logException(new CommandException(ERROR_CLASS_NOT_FOUNT, e)
				.addContext(COMMAND, command).addContext(NAME, name),
				ERROR_CLASS_NOT_FOUNT_MESSAGE, name);
	}

}
