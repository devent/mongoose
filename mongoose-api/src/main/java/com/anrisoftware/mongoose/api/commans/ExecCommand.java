package com.anrisoftware.mongoose.api.commans;

/**
 * External command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ExecCommand extends Command {

	/**
	 * Returns the exit value of the command.
	 */
	int getTheExitValue();

}
