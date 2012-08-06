package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFoundException;

/**
 * The {@code cd} build-in command that will change the current working
 * directory to the user home directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserHomeCd extends CdBuildin {

	public UserHomeCd(CdBuildin parent) {
		super(parent);
		setEnvironment(parent.getEnvironment());
	}

	@Override
	ReturnValue callBuildin() throws Exception {
		File dir = getEnvironment().getUserHome();
		if (!dir.isDirectory()) {
			throw new DirectoryNotFoundException(dir);
		}
		getEnvironment().setWorkingDirectory(dir);
		return super.callBuildin();
	}
}
