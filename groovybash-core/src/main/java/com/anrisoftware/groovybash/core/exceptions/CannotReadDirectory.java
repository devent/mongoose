package com.anrisoftware.groovybash.core.exceptions;

import static java.lang.String.format;

import java.io.File;
import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class CannotReadDirectory extends FileNotFoundException {

	private final File dir;

	public CannotReadDirectory(File dir) {
		super(format("The directory %s cannot be read.", dir));
		this.dir = dir;
	}

	public File getDirectory() {
		return dir;
	}

}
