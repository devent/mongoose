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
package com.anrisoftware.mongoose.api.exceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Exception that the directory could not be found. Either the path is not a
 * directory or the directory is not readable.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DirectoryNotFoundException extends FileNotFoundException {

	private static final String DIRECTORY_KEY = "directory";

	private static final String DIRECTORY_COULD_NOT_FOUND = "Directory could not be found.";

	private Context<DirectoryNotFoundException> context;

	/**
	 * Sets the path of the directory.
	 * 
	 * @param dir
	 *            the {@link File} path of the directory.
	 */
	public DirectoryNotFoundException(File dir) {
		super(DIRECTORY_COULD_NOT_FOUND);
		addContext(DIRECTORY_KEY, dir);
	}

	/**
	 * Returns the directory that could not be found.
	 * 
	 * @return the {@link File} directory.
	 */
	public File getTheDirectory() {
		return (File) context.getContext().get(DIRECTORY_KEY);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public DirectoryNotFoundException addContext(String name, Object value) {
		return context.addContext(name, value);
	}

	/**
	 * @see Context#getContext()
	 */
	public Map<String, Object> getContext() {
		return context.getContext();
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
