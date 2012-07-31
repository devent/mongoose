package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import static com.anrisoftware.groovybash.core.buildins.DefaultReturnValue.SUCCESS_VALUE;

import java.io.File;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFound;

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
	}

	@Override
	ReturnValue callBuildin() throws Exception {
		File dir = getEnvironment().getUserHome();
		if (!dir.isDirectory()) {
			throw new DirectoryNotFound(dir);
		}
		getEnvironment().setWorkingDirectory(dir);
		return SUCCESS_VALUE;
	}
}
