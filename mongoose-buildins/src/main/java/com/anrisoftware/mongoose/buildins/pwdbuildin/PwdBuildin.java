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
package com.anrisoftware.mongoose.buildins.pwdbuildin;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoos.command.AbstractCommand;
import com.anrisoftware.mongoose.api.exceptions.ExecutionException;

/**
 * The build-in command {@code pwd}. Prints the absolute path of the current
 * working directory to the standard output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PwdBuildin extends AbstractCommand {

	private final PwdBuildinLogger log;

	@Inject
	PwdBuildin(PwdBuildinLogger logger) {
		this.log = logger;
	}

	/**
	 * Returns the current working directory.
	 */
	@Override
	public PwdBuildin call() throws ExecutionException {
		PrintStream stream = new PrintStream(getOutput());
		stream.println(getEnvironment().getWorkingDirectory());
		stream.flush();
		return this;
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArguments(this, args.size());
		log.checkArguments(this, unnamedArgs.size());
	}

	/**
	 * Returns the name {@code pwd}.
	 */
	@Override
	public String getName() {
		return PwdService.ID;
	}
}
