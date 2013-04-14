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
package com.anrisoftware.mongoose.buildins.cdbuildin;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.mongoos.command.AbstractCommand;
import com.anrisoftware.mongoose.api.commans.Environment;
import com.anrisoftware.mongoose.api.exceptions.ExecutionException;

/**
 * The build-in command {@code cd directory}. Change the current working
 * directory to the specified directory. The directory defaults to the user home
 * directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CdBuildin extends AbstractCommand {

	private static final String NAME = "cd";

	private final CdBuildinLogger log;

	private File directory;

	/**
	 * @param logger
	 *            the {@link CdBuildinLogger} for logging messages;
	 */
	@Inject
	CdBuildin(CdBuildinLogger logger) {
		this.log = logger;
	}

	@Override
	public CdBuildin call() throws ExecutionException {
		getEnvironment().setWorkingDirectory(directory);
		log.changedDirectory(directory);
		return this;
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		this.directory = environment.getUserHome();
	}

	@Override
	public void setArgs(Object args) throws Exception {
		super.setArgs(args);
		this.directory = asFile(getArgsList().get(0));
		log.checkDirectory(directory);
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		} else {
			return new File(object.toString());
		}
	}

	/**
	 * Returns the name {@value #NAME}.
	 */
	@Override
	public String getName() {
		return NAME;
	}
}