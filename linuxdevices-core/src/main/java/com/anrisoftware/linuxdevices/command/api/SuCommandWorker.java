package com.anrisoftware.linuxdevices.command.api;

/**
 * Execute a command as a new process in the context of the root user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface SuCommandWorker extends CommandWorker {

	/**
	 * Sets the command to run specified commands in the context of the root
	 * user.
	 * 
	 * @param sudoCommand
	 *            the sudo command.
	 */
	void setSudoCommand(String sudoCommand);

	/**
	 * Returns the command to run specified commands in the context of the root
	 * user.
	 * 
	 * @return the sudo command.
	 */
	String getSudoCommand();

	/**
	 * Sets the callback to check if the current user is the root user.
	 * 
	 * @param isRootUser
	 *            the {@link CheckIsRootUser} callback.
	 */
	void setCheckIsRootUser(CheckIsRootUser isRootUser);
}
