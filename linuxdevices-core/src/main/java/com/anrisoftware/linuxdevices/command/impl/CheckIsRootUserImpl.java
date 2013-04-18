package com.anrisoftware.linuxdevices.command.impl;

import com.anrisoftware.linuxdevices.command.api.CheckIsRootUser;

/**
 * Checks if the current user is the root user by comparing the current user
 * name. If the current user name equals "root" it should be the root user.
 * <p>
 * Other ways to check is to execute the command "whoami" or "id -u". See <a
 * href=http://stackoverflow.com/questions/5117449/check-if-a-user-is-root-in-a-
 * java-application>Check if a user is root in a java application
 * (stackoverflow.com)</a>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CheckIsRootUserImpl implements CheckIsRootUser {

	@Override
	public boolean isRoot() {
		return "root".equalsIgnoreCase(System.getProperty("user.name"));
	}

}
