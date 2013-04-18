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
package com.anrisoftware.mongoose.buildins.runbuildin;

import static org.apache.commons.lang3.StringUtils.split;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.Executor;

import com.anrisoftware.mongoos.command.AbstractCommand;
import com.anrisoftware.mongoose.api.commans.Environment;
import com.csvreader.CsvReader;

/**
 * Executes the specified command in a separate process with the specified
 * environment and working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RunBuildin extends AbstractCommand {

	private final RunBuildinLogger log;

	private final Executor executor;

	private CommandLine command;

	private Map<String, String> env;

	private File workingDirectory;

	private ExecuteResultHandler handler;

	@Inject
	RunBuildin(RunBuildinLogger logger, Executor executor) {
		this.log = logger;
		this.executor = executor;
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		this.workingDirectory = environment.getWorkingDirectory();
		this.env = environment.getEnv();
	}

	@Override
	public RunBuildin call() throws Exception {
		startProcess();
		return this;
	}

	private void startProcess() throws IOException {
		executor.setWorkingDirectory(workingDirectory);
		executor.execute(command, env, handler);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		super.argumentsSet(args, unnamedArgs);

	}

	@Override
	protected void setupArguments() throws Exception {
		Object[] args = getArgs();
		command = getCommand(args);
		env = getEnvironment(args);
		workingDirectory = getWorkingDir(args);
		redirectErrorStream = getFlag(REDIRECT_ERROR_STREAM_FLAG, false);
	}

	private List<String> getCommand(Object[] args) {
		log.checkCommandArgumentIndex(args);
		Reader reader = new StringReader(args[0].toString());
		CsvReader csv = new CsvReader(reader, ' ');
		try {
			csv.readRecord();
			return Arrays.asList(csv.getValues());
		} catch (IOException e) {
			throw log.errorReadCommandString(this, e);
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
