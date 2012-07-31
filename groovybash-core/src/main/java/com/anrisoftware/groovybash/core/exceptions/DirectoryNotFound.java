package com.anrisoftware.groovybash.core.exceptions;

import static java.lang.String.format;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Exception that the directory could not be found. Either the path is not a
 * directory or the directory is not readable.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DirectoryNotFound extends FileNotFoundException {

	private final File dir;

	/**
	 * Sets the path of the directory.
	 * 
	 * @param dir
	 *            the {@link File} path of the directory.
	 */
	public DirectoryNotFound(File dir) {
		super(format("The directory %s could not be found.", dir));
		this.dir = dir;
	}

	/**
	 * Returns the directory that could not be found.
	 * 
	 * @return the {@link File} directory.
	 */
	public File getDirectory() {
		return dir;
	}

}
