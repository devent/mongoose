package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

class FileCd extends CdBuildin {

	private final File dir;

	public FileCd(AbstractBuildin parent, File dir) {
		super(new StandardStreams(parent.getInputStream(),
				parent.getOutputStream(), parent.getErrorStream()));
		this.dir = dir;
		setEnvironment(parent.getEnvironment());
	}

	@Override
	CdBuildin callBuildin() {
		getEnvironment().setWorkingDirectory(dir);
		return this;
	}
}
