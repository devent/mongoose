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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.ExecutionException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code export}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExportBuildin extends AbstractCommand {

	private final ExportBuildinLogger log;

	private final ExportWorker worker;

	private Map<String, String> currentEnv;

	private HashMap<String, String> env;

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
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		currentEnv = environment.getEnv();
	}

	@Override
	protected void doCall() throws ExecutionException {
		PrintStream out = new PrintStream(getOutput());
		worker.doEnv(currentEnv, env, out);
		out.flush();
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		env = new HashMap<String, String>();
		if (unnamedArgs.size() == 1) {
		}
	}

	/**
	 * Returns the name {@code export}.
	 */
	@Override
	public String getTheName() {
		return ExportService.ID;
	}
}
