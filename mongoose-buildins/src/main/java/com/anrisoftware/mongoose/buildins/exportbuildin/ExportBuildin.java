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
package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.api.exceptions.ExecutionException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code export}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExportBuildin extends AbstractCommand {

	private static final String FLAG_VALUE = "1";

	private final ExportBuildinLogger log;

	private ExportWorker worker;

	private Map<String, String> currentEnv;

	private Map<String, String> env;

	/**
	 * @param logger
	 *            the {@link ExportBuildinLogger} for logging messages;
	 */
	@Inject
	ExportBuildin(ExportBuildinLogger logger) {
		this.log = logger;
		this.worker = new PrintEnv();
	}

	@Override
	public void setEnvironment(Environment environment) throws CommandException {
		super.setEnvironment(environment);
		currentEnv = environment.getEnv();
	}

	@Override
	protected void doCall() throws ExecutionException {
		worker.doEnv(currentEnv, env, getOutput());
		getTheEnvironment().setEnv(currentEnv);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		env = new HashMap<String, String>();
		setupUnnamed(unnamedArgs);
		setupNamed(args);
	}

	private void setupNamed(Map<String, Object> args) {
		if (args.size() > 1) {
			worker = new SetEnv();
		}
		if (args.containsKey("remove")) {
			setRemove((Boolean) args.get("remove"));
		}
		parseEnv(env, args);
	}

	private void setupUnnamed(List<Object> unnamedArgs) {
		if (unnamedArgs.size() > 0) {
			parseEnv(env, unnamedArgs);
			worker = new SetEnv();
		}
	}

	private void parseEnv(Map<String, String> env, Map<String, Object> args) {
		for (Map.Entry<String, Object> values : args.entrySet()) {
			if (values.getKey().equals("remove")) {
				continue;
			}
			if (values.getKey().equals("unnamed")) {
				continue;
			}
			env.put(values.getKey(), values.getValue().toString());
		}
	}

	private void parseEnv(Map<String, String> env, List<Object> list) {
		for (Object object : list) {
			env.put(object.toString(), FLAG_VALUE);
		}
	}

	/**
	 * Sets whether to remove the environment variables.
	 * 
	 * @param remove
	 *            set to {@code true} to remove the variables.
	 */
	public void setRemove(boolean remove) {
		if (remove) {
			worker = new RemoveEnv();
		}
		log.removeSet(this, remove);
	}

	/**
	 * Returns the name {@code export}.
	 */
	@Override
	public String getTheName() {
		return ExportService.ID;
	}
}
