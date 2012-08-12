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
package com.anrisoftware.groovybash.buildins.runbuildin;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.buildins.returns.ReturnCodeFactory;
import com.anrisoftware.groovybash.core.Environment;
import com.anrisoftware.groovybash.core.ReturnValue;
import com.csvreader.CsvReader;

/**
 * Executes the specified command in a separate process with the specified
 * environment and working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class RunBuildin extends AbstractBuildin {

	private static final String REDIRECT_ERROR_STREAM_FLAG = "redirectErrorStream";

	private List<String> command;

	private Map<String, String> environment;

	private File workingDirectory;

	private boolean redirectErrorStream;

	private final OutputTaskFactory outputTaskFactory;

	private final ReturnCodeFactory returnCodeFactory;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	RunBuildin(StandardStreams streams, ReturnCodeFactory returnCodeFactory,
			OutputTaskFactory outputTaskFactory) {
		super(streams);
		this.returnCodeFactory = returnCodeFactory;
		this.outputTaskFactory = outputTaskFactory;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		Process process = startProcess();
		Environment env = getEnvironment();
		Future<?> outputTask = env.submitTask(outputTaskFactory.create(
				process.getInputStream(), getOutputStream()));
		Future<?> errorTask = env.submitTask(outputTaskFactory.create(
				process.getErrorStream(), getErrorStream()));
		outputTask.get();
		errorTask.get();
		int ret = process.waitFor();
		return returnCodeFactory.create(ret);
	}

	private Process startProcess() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(command);
		Map<String, String> parentEnvironment = builder.environment();
		parentEnvironment.putAll(environment);
		builder.directory(workingDirectory);
		builder.redirectErrorStream(redirectErrorStream);
		Process process = builder.start();
		return process;
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		command = getCommand(args);
		environment = getEnvironment(args);
		workingDirectory = getWorkingDir(args);
		redirectErrorStream = getFlag(REDIRECT_ERROR_STREAM_FLAG, false);
	}

	private List<String> getCommand(Object[] args) {
		Reader reader = new StringReader(args[0].toString());
		CsvReader csv = new CsvReader(reader, ' ');
		try {
			csv.readRecord();
			return Arrays.asList(csv.getValues());
		} catch (IOException e) {
			throw new IllegalStateException(
					"Reading from string should always be successfull.", e);
		}
	}

	private Map<String, String> getEnvironment(Object[] args) {
		if (args.length < 2) {
			return newHashMap();
		}
		if (args[1] instanceof Map) {
			return asMap(args[1]);
		} else {
			return parseEnvironment(args);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> asMap(Object obj) {
		return (Map<String, String>) obj;
	}

	private Map<String, String> parseEnvironment(Object[] args) {
		Map<String, String> map = newHashMap();
		for (String tuple : split(args[1].toString(), " ;,")) {
			String[] entry = split(tuple, "=");
			map.put(entry[0], entry[1]);
		}
		return map;
	}

	private File getWorkingDir(Object[] args) {
		if (args.length < 3) {
			return getEnvironment().getWorkingDirectory();
		}
		if (args[2] instanceof File) {
			return (File) args[2];
		} else {
			return new File(args[2].toString());
		}
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "run";
	}
}
