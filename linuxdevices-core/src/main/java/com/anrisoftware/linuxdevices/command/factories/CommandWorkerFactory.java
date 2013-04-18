package com.anrisoftware.linuxdevices.command.factories;

import com.anrisoftware.linuxdevices.command.api.CommandWorker;
import com.anrisoftware.linuxdevices.command.api.StandardStreams;

/**
 * Factory to create a worker that executes a command as a new process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface CommandWorkerFactory {

	/**
	 * create a worker that executes the specified command as a new process.
	 * 
	 * @param command
	 *            the command and the command arguments. The arguments are
	 *            separated by whitespace and can be put in double quotes.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard output,
	 *            error and input streams.
	 * 
	 * @return the {@link CommandWorker}.
	 */
	CommandWorker create(String command, StandardStreams streams);
}
