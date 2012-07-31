package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

/**
 * The {@code cd} build-in command that will change the current working
 * directory to the user home directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserHomeCd extends CdBuildin {

	public UserHomeCd(AbstractBuildin parent) {
		super(new StandardStreams(parent.getInputStream(),
				parent.getOutputStream(), parent.getErrorStream()));
		setEnvironment(parent.getEnvironment());
		setArguments(parent.getArgs());
	}

	@Override
	CdBuildin callBuildin() {
		File home = getEnvironment().getUserHome();
		getEnvironment().setWorkingDirectory(home);
		return this;
	}
}
