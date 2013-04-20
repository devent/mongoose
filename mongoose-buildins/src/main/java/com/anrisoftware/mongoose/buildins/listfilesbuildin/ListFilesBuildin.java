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
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.reflect.ConstructorUtils.getAccessibleConstructor;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;
import groovy.lang.Closure;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.inject.Inject;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.anrisoftware.mongoose.api.exceptions.ExecutionException;
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

	@Inject
	ListFilesBuildin(ListFilesBuildinLogger logger) {
		this.log = logger;
		this.listFiles = new ListFiles();
		this.directories = new ArrayList<File>();
		this.patterns = new ArrayList<String>();
		setFilterType(WildcardFileFilter.class);
		listFiles.setDirFilter(FALSE);
	}

	@Override
	public String getTheName() {
		return ListFilesService.ID;
	}

	@Override
	public ListFilesBuildin call() throws ExecutionException {
		files = listFiles.build();
		return this;
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
		for (File dir : directories) {
			listFiles.addDirectory(dir);
		}
	}

	private void setupFiltersFromPatterns() throws Exception {
		for (String pattern : patterns) {
			FileFilter filter = invokeConstructor(filterType, pattern);
			listFiles.addFilter(filter);
		}
	}

	private void splitDirectories(List<Object> list) {
		for (Object object : list) {
			String path = object.toString();
			String[] paths = split(path, File.separator);
			File file = null;
			int i = 0;
			for (; i < paths.length; i++) {
				String string = paths[i];
				if (!isPattern(string)) {
					file = recursiveDir(file, string);
				} else {
					break;
				}
			}
			if (file != null) {
				directories.add(file);
			}
			if (i < paths.length) {
				patterns.add(paths[i]);
			}
		}
	}

	private File recursiveDir(File file, String string) {
		if (file == null) {
			return new File(string);
		} else {
			File dir = new File(file, string);
			return dir.isDirectory() ? dir : file;
		}
	}

	private boolean isPattern(String string) {
		try {
			Pattern.compile(string);
			return true;
		} catch (PatternSyntaxException e) {
			return false;
		}
	}

	public void setFilterType(Class<? extends FileFilter> type) {
		this.filterType = type;
		Constructor<?> c = getAccessibleConstructor(type, String.class);
		log.checkCtor(this, c, type);
	}

	private void setFilter(Object filter) {
		if (filter instanceof FileFilter) {
			setFilter((FileFilter) filter);
		}
		if (filter instanceof Closure<?>) {
			setFilter((Closure<?>) filter);
		}
	}

	public void setFilter(Closure<?> filter) {
		log.checkFilter(this, filter);
		setFilter(new ClosureFileFilter(filter));
	}

	public void setFilter(FileFilter filter) {
		log.checkFilter(this, filter);
		listFiles.setFilter(filter);
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
		listFiles.setDirFilter(TRUE);
		log.recursiveSet(this, recursive);
	}

	public void setIncludeSubDirectories(boolean include) {
		listFiles.setIncludeSubDirectories(include);
		log.includeSubDirectoriesSet(this, include);
	}
}
