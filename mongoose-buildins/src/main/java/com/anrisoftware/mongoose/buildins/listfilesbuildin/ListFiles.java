/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.buildins.listfilesbuildin;

import static com.anrisoftware.mongoose.buildins.listfilesbuildin.ListFilesUtil.listFiles;
import static org.apache.commons.io.filefilter.FileFilterUtils.asFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.lang3.builder.Builder;

/**
 * List the files in the specified directories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ListFiles implements Builder<List<File>> {

	private final List<File> directories;

	private OrFileFilter fileFilter;

	private IOFileFilter dirFilter;

	private int depth;

	private boolean includeSubDirectories;

	ListFiles() {
		this.includeSubDirectories = false;
		this.depth = 0;
		this.directories = new ArrayList<File>();
		this.fileFilter = new OrFileFilter();
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setIncludeSubDirectories(boolean include) {
		this.includeSubDirectories = include;
	}

	/**
	 * Adds the file filter for the files.
	 * 
	 * @param filter
	 *            the {@link IOFileFilter}.
	 */
	public void addFilter(IOFileFilter filter) {
		fileFilter.addFileFilter(filter);
	}

	/**
	 * Adds the file filter for the files.
	 * 
	 * @param filter
	 *            the {@link FileFilter}.
	 */
	public void addFilter(FileFilter filter) {
		addFilter(asFileFilter(filter));
	}

	/**
	 * Sets the file filter for the files.
	 * 
	 * @param filter
	 *            the {@link IOFileFilter}.
	 */
	public void setFilter(IOFileFilter filter) {
		this.fileFilter = new OrFileFilter();
		fileFilter.addFileFilter(filter);
	}

	/**
	 * Sets the file filter for the files.
	 * 
	 * @param filter
	 *            the {@link FileFilter}.
	 */
	public void setFilter(FileFilter filter) {
		setFilter(asFileFilter(filter));
	}

	/**
	 * Sets the file filter for the directories.
	 * 
	 * @param filter
	 *            the {@link IOFileFilter}.
	 */
	public void setDirFilter(IOFileFilter filter) {
		this.dirFilter = filter;
	}

	/**
	 * Sets the file filter for the directories.
	 * 
	 * @param filter
	 *            the {@link FileFilter}.
	 */
	public void setDirFilter(FileFilter filter) {
		setDirFilter(asFileFilter(filter));
	}

	/**
	 * Appends a directory where the files should be searched.
	 * 
	 * @param dir
	 *            the {@link File} directory.
	 */
	public void addDirectory(File dir) {
		directories.add(dir);
	}

	@Override
	public List<File> build() {
		List<File> files = new ArrayList<File>(10 * directories.size());
		for (File dir : directories) {
			files.addAll(listFiles(dir, fileFilter, dirFilter, depth,
					includeSubDirectories));
		}
		return files;
	}

}
