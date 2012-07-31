package com.anrisoftware.groovybash.core.exceptions;

import static java.lang.String.format;

import java.io.File;
import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class DirectoryNotFound extends FileNotFoundException {

	private final File dir;

	public DirectoryNotFound(File dir) {
		super(format("The directory %s could not be found.", dir));
		this.dir = dir;
	}

	public File getDirectory() {
		return dir;
	}

}
