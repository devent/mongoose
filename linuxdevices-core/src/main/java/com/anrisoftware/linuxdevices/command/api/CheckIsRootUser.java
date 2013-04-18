package com.anrisoftware.linuxdevices.command.api;

/**
 * Checks if the current user is the root user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface CheckIsRootUser {

	/**
	 * Checks if the current user is the root user.
	 * 
	 * @return {@code true} if the current user is the root user, {@code false}
	 *         if not.
	 */
	boolean isRoot();
}
