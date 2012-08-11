package com.anrisoftware.groovybash.buildins.cdbuildin;

import java.io.File;

import com.anrisoftware.groovybash.core.ReturnValue;
import com.anrisoftware.groovybash.core.exceptions.DirectoryNotFoundException;

class FileCd extends CdBuildin {

	private final File dir;

	public FileCd(CdBuildin parent, File dir) {
		super(parent);
		this.dir = dir;
		setEnvironment(parent.getEnvironment());
	}

	@Override
	ReturnValue callBuildin() throws Exception {
		if (!dir.isDirectory()) {
			throw new DirectoryNotFoundException(dir);
		}
		getEnvironment().setWorkingDirectory(dir);
		return super.callBuildin();
	}
}
