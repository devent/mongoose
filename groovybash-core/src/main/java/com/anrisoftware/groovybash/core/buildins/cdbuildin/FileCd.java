package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import static com.anrisoftware.groovybash.core.buildins.DefaultReturnValue.SUCCESS_VALUE;

import java.io.File;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.DefaultReturnValue;
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
	ReturnValue callBuildin() {
		if (!dir.isDirectory()) {
			return new DefaultReturnValue(false, new DirectoryNotFound(dir));
		}
		getEnvironment().setWorkingDirectory(dir);
		return SUCCESS_VALUE;
	}
}
