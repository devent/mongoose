/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.core.exceptions;

import static java.lang.String.format;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Exception that the directory could not be found. Either the path is not a
 * directory or the directory is not readable.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class DirectoryNotFoundException extends FileNotFoundException {

	private final File dir;

	/**
	 * Sets the path of the directory.
	 * 
	 * @param dir
	 *            the {@link File} path of the directory.
	 */
	public DirectoryNotFoundException(File dir) {
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
