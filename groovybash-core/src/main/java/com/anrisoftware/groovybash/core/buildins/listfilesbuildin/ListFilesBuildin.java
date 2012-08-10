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

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

/**
 * The build-in command {@code listFiles NAME...}. Search all files in the
 * current working directory with the given file names {@code NAME...}. The file
 * names can contain wildcats {@code *?}. If no names are specified, all files
 * are returned.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class ListFilesBuildin extends AbstractBuildin {

	private final WildcardListFactory wildcardListFactory;

	private final FilesReturnValueFactory filesReturnValueFactory;

	private final List<ListWorker> listFilesWorkers;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	ListFilesBuildin(StandardStreams streams,
			FilesReturnValueFactory filesReturnValueFactory,
			WildcardListFactory wildcardListFactory) {
		super(streams);
		this.filesReturnValueFactory = filesReturnValueFactory;
		this.wildcardListFactory = wildcardListFactory;
		this.listFilesWorkers = newArrayList();
		this.listFilesWorkers.add(wildcardListFactory.create("*"));
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		List<File> files = newArrayList();
		File directory = getEnvironment().getWorkingDirectory();
		for (ListWorker worker : listFilesWorkers) {
			files.addAll(worker.listFiles(directory));
		}
		return filesReturnValueFactory.create(files);
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (getArgs().length < 1) {
			return;
		}
		listFilesWorkers.clear();
		for (Object a : args) {
			if (a instanceof List) {
				addWildcatsInList(a);
			} else {
				listFilesWorkers.add(wildcardListFactory.create(a.toString()));
			}
		}
	}

	private void addWildcatsInList(Object a) {
		List<Object> list = asList(a);
		for (Object object : list) {
			listFilesWorkers.add(wildcardListFactory.create(object.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object> asList(Object a) {
		return (List<Object>) a;
	}

	/**
	 * Returns the name {@code listFiles}.
	 */
	@Override
	public String getName() {
		return "listFiles";
	}

	@Override
	public String toString() {
		return getName();
	}
}
