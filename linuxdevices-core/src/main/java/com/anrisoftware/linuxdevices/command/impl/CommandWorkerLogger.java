package com.anrisoftware.linuxdevices.command.impl;

import static java.lang.String.format;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link CommandWorkerImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CommandWorkerLogger extends AbstractLogger {

	/**
	 * Creates the logger for {@link CommandWorkerImpl}.
	 */
	CommandWorkerLogger() {
		super(CommandWorkerImpl.class);
	}

	void startingCommand(List<String> command) {
		log.trace("Starting the command {} in a new process...", command.get(0));
	}

	void commandFinish(List<String> command, int exitCode) {
		log.debug("Command {} finish with exit code {}.", command.get(0),
				exitCode);
	}

	RuntimeException errorReadCommandString(IOException e) {
		IllegalStateException ex = new IllegalStateException(
				"Reading from string should always be successfull.", e);
		log.error("", ex);
		return ex;
	}

	IOException unexpectedErrorOutputTask(String command, ExecutionException e) {
		IOException ex = new IOException(format(
				"Unexpected error in the output task for the command ``%s''",
				command), e);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	IOException interruptedErrorOutputTask(String command,
			InterruptedException e) {
		IOException ex = new IOException(
				format("Interrupted while reading or writing data from stream for the command ``%s''",
						command), e);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	IOException interruptedErrorProcess(String command, InterruptedException e) {
		IOException ex = new IOException(format(
				"Interrupted while waiting for the command ``%s''", command), e);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

}
