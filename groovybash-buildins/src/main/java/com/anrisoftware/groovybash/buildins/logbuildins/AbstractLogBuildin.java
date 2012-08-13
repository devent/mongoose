package com.anrisoftware.groovybash.buildins.logbuildins;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * Log messages in the right logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractLogBuildin extends AbstractBuildin {

	private LogBuildinLogger log;

	private String message;

	private Object[] arguments;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	AbstractLogBuildin(StandardStreams streams) {
		super(streams);
	}

	/**
	 * Injects the logger.
	 * 
	 * @param logger
	 *            the {@link LogBuildinLogger}.
	 */
	@Inject
	public void setLogBuildinLogger(LogBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		Logger logger = getEnvironment().getScriptLogger();
		logMessage(logger);
		return returnCodeFactory.createSuccess();
	}

	/**
	 * Log the message with the arguments in the right logging level.
	 * 
	 * @param logger
	 *            the {@link Logger}.
	 * 
	 * @see #getMessage()
	 * @see #getArguments()
	 */
	protected abstract void logMessage(Logger logger);

	@Override
	protected void setupArguments() throws Exception {
		Object[] args = getArgs();
		log.checkMinimumArgs(this, args);
		message = args[0].toString();
		arguments = createArguments(args);
	}

	private Object[] createArguments(Object[] args) {
		if (args.length < 2) {
			return new Object[0];
		}
		Object[] arguments = new Object[args.length - 1];
		System.arraycopy(args, 1, arguments, 0, arguments.length);
		return arguments;
	}

	/**
	 * Returns the logging message.
	 * 
	 * @return the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the arguments for the logging message.
	 * 
	 * @return the arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}

	/**
	 * Returns the name {@code debug}.
	 */
	@Override
	public String getName() {
		return "debug";
	}
}
