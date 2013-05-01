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
package com.anrisoftware.mongoose.buildins.listfilesbuildin;

import static org.apache.commons.io.filefilter.FalseFileFilter.FALSE;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;
import static org.apache.commons.lang3.reflect.ConstructorUtils.getAccessibleConstructor;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;
import groovy.lang.Closure;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code listFiles}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ListFilesBuildin extends AbstractCommand {

	private static final String FILTER_TYPE_KEY = "filterType";

	private static final String FILTER_KEY = "filter";

	private static final String INCLUDE_SUB_DIRECTORIES_KEY = "includeSubDirectories";

	private static final String DEPTH_KEY = "depth";

	private static final String RECURSIVE_KEY = "recursive";

	private final ListFilesBuildinLogger log;

	private final ListFiles listFiles;

	private final ArrayList<File> directories;

	private final ArrayList<String> patterns;

	private List<File> files;

	private Class<? extends FileFilter> filterType;

	private FileFilter filter;

	@Inject
	ListFilesBuildin(ListFilesBuildinLogger logger) {
		this.log = logger;
		this.listFiles = new ListFiles();
		this.directories = new ArrayList<File>();
		this.patterns = new ArrayList<String>();
		this.filter = null;
		setFilterType(WildcardFileFilter.class);
		listFiles.setDirFilter(FALSE);
	}

	@Override
	public String getTheName() {
		return ListFilesService.ID;
	}

	@Override
	protected void doCall() {
		files = listFiles.build();
	}

	/**
	 * Returns the found files.
	 * 
	 * @return the {@link List} of the found files.
	 */
	public List<File> getTheFiles() {
		return files;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		if (args.containsKey(RECURSIVE_KEY)) {
			setRecursive((Boolean) args.get(RECURSIVE_KEY));
		}
		if (args.containsKey(DEPTH_KEY)) {
			setDepth((Integer) args.get(DEPTH_KEY));
		}
		if (args.containsKey(INCLUDE_SUB_DIRECTORIES_KEY)) {
			setIncludeSubDirectories((Boolean) args
					.get(INCLUDE_SUB_DIRECTORIES_KEY));
		}
		if (args.containsKey(FILTER_KEY)) {
			setFilter(args.get(FILTER_KEY));
		}
		if (args.containsKey(FILTER_TYPE_KEY)) {
			setFilterType((Class<? extends FileFilter>) args
					.get(FILTER_TYPE_KEY));
		}
		splitDirectories(unnamedArgs);
		setupFiltersFromPatterns();
		setupDirectories();
	}

	private void setupDirectories() {
		if (directories.isEmpty()) {
			directories.add(getTheEnvironment().getWorkingDirectory());
		}
		for (File dir : directories) {
			listFiles.addDirectory(dir);
			log.addDirectory(this, dir);
		}
	}

	private void setupFiltersFromPatterns() throws Exception {
		if (filter != null) {
			return;
		}
		if (patterns.isEmpty()) {
			patterns.add("*");
		}
		for (String pattern : patterns) {
			FileFilter filter = invokeConstructor(filterType, pattern);
			listFiles.addFilter(filter);
			log.addFilterPattern(this, pattern);
		}
	}

	private void splitDirectories(List<Object> list) {
		for (Object object : list) {
			File path = asFile(object);
			File parent = path.getParentFile();
			String pattern = path.getName();
			while (parent != null) {
				String name = parent.getName();
				if (!parent.isDirectory()) {
					pattern += File.separator + name;
				} else {
					directories.add(parent);
					break;
				}
				parent = parent.getParentFile();
			}
			patterns.add(pattern);
		}
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		} else {
			return new File(object.toString());
		}
	}

	public void setFilterType(Class<? extends FileFilter> type) {
		this.filterType = type;
		Constructor<?> c = getAccessibleConstructor(type, String.class);
		log.checkCtor(this, c, type);
	}

	private void setFilter(Object filter) {
		log.checkFilter(this, filter);
		if (filter instanceof FileFilter) {
			this.filter = (FileFilter) filter;
			listFiles.setFilter(this.filter);
		}
		if (filter instanceof Closure<?>) {
			this.filter = new ClosureFileFilter((Closure<?>) filter);
			listFiles.setFilter(this.filter);
		}
		log.filterSet(this, filter);
	}

	public void setDepth(int depth) {
		log.checkDepth(this, depth);
		if (depth == -1) {
			listFiles.setDepth(Integer.MAX_VALUE);
		} else {
			listFiles.setDepth(depth);
		}
		log.depthSet(this, depth);
	}

	public void setRecursive(boolean recursive) {
		if (listFiles.getDepth() == 0) {
			listFiles.setDepth(Integer.MAX_VALUE);
		}
		listFiles.setDirFilter(TRUE);
		log.recursiveSet(this, recursive);
	}

	public void setIncludeSubDirectories(boolean include) {
		if (include) {
			listFiles.setDirFilter(TRUE);
			listFiles.setIncludeSubDirectories(true);
		} else {
			listFiles.setIncludeSubDirectories(false);
		}
		log.includeSubDirectoriesSet(this, include);
	}
}
