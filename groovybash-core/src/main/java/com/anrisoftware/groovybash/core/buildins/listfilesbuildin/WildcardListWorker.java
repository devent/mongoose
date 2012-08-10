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
package com.anrisoftware.groovybash.core.buildins.listfilesbuildin;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.google.inject.assistedinject.Assisted;

/**
 * List files with a wildcard.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WildcardListWorker implements ListWorker {

	private final String name;

	/**
	 * Sets the the wildcard name.
	 * 
	 * @param name
	 *            the wildcard name.
	 */
	@Inject
	WildcardListWorker(@Assisted String name) {
		this.name = name;
	}

	@Override
	public List<File> listFiles(File dir) {
		FileFilter filter = new WildcardFileFilter(name);
		return asList(dir.listFiles(filter));
	}

}
