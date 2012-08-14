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
package com.anrisoftware.groovybash.buildins.detoxbuildin;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.common.collect.Lists;

/**
 * The build-in command {@code detox file}. Remove all ``toxic'' characters from
 * a file name. The {@code file} argument can be a \link{File} or the file name.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class DetoxBuildin extends AbstractBuildin {

	private static final String WHITESPACE = "\\p{Space}";

	private static final String PUNCTUATION_BASH = "\"'`";

	private static final String CONTROL = "\\p{Cntrl}";

	private static final String PUNCTUATION_SYS = "/\\\\;:";

	private final List<ReplaceString> replaceList;

	private String fileName;

	private File parentFile;

	private final FileValueFactory valueFactory;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	DetoxBuildin(StandardStreams streams, FileValueFactory valueFactory) {
		super(streams);
		this.replaceList = createReplaceList();
		this.valueFactory = valueFactory;
	}

	private List<ReplaceString> createReplaceList() {
		List<ReplaceString> list = Lists.newArrayList();
		list.add(new TrimString());
		list.add(new PatternReplace("_", "[%s%s%s]", WHITESPACE,
				PUNCTUATION_BASH, PUNCTUATION_SYS));
		list.add(new PatternReplace("", "[%s]", CONTROL));
		list.add(new PatternReplace("_", "_+"));
		return list;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		String string = fileName;
		for (ReplaceString replace : replaceList) {
			string = replace.replaceString(string);
		}
		return valueFactory.create(parentFile, string);
	}

	@Override
	protected void setupArguments() throws Exception {
		Object arg = getArgs()[0];
		if (arg instanceof File) {
			File file = (File) arg;
			parentFile = file.getParentFile();
			fileName = file.getName();
		} else {
			fileName = arg.toString();
		}
	}

	/**
	 * Returns the name {@code detox}.
	 */
	@Override
	public String getName() {
		return "detox";
	}
}
