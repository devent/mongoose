package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFound;

class FileCd extends CdBuildin {

	private final File dir;

	public FileCd(AbstractBuildin parent, File dir) {
		super(new StandardStreams(parent.getInputStream(),
				parent.getOutputStream(), parent.getErrorStream()));
		this.dir = dir;
		setEnvironment(parent.getEnvironment());
	}

	@Override
	ReturnValue callBuildin() throws Exception {
		if (!dir.isDirectory()) {
			throw new DirectoryNotFound(dir);
		}
		getEnvironment().setWorkingDirectory(dir);
		return super.callBuildin();
	}
}
