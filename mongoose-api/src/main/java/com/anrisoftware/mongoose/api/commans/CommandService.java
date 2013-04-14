package com.anrisoftware.mongoose.api.commans;

/**
 * Makes the command available as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CommandService {

	/**
	 * Returns the information about the command.
	 * 
	 * @return the {@link CommandInfo}.
	 */
	CommandInfo getInfo();

	/**
	 * Returns the command factory.
	 * 
	 * @param external
	 *            optional external dependencies for the command.
	 * 
	 * @return the {@link CommandFactory}.
	 */
	CommandFactory getCommandFactory(Object... external);
}
