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
package com.anrisoftware.groovybash.buildins.listfilesbuildin;

import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns the found files.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class FilesReturnValue extends GroovyObjectSupport implements ReturnValue {

	private final List<File> files;

	/**
	 * Sets the found files.
	 * 
	 * @param files
	 *            the {@link Collection} containing the found files.
	 */
	@Inject
	public FilesReturnValue(@Assisted Collection<File> files) {
		this.files = ImmutableList.copyOf(files);
	}

	/**
	 * Returns the found files.
	 * 
	 * @return an immutable {@link List} containing the files.
	 */
	public List<File> getFiles() {
		return files;
	}

	@Override
	public String toString() {
		return files.toString();
	}
}
