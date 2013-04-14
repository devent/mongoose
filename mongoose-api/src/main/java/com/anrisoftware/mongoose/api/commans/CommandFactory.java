package com.anrisoftware.mongoose.api.commans;

/**
 * Factory to create the command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CommandFactory {

	/**
	 * Creates the command.
	 * 
	 * @return the {@link Command}.
	 */
	Command create();
}
