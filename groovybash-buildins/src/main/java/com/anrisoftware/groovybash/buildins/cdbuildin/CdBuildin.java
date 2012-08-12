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
package com.anrisoftware.groovybash.buildins.cdbuildin;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.Environment;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * The build-in command {@code cd [DIR]}. Change the current working directory
 * to the specified directory {@code DIR}. {@code DIR} defaults to the user home
 * directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CdBuildin extends AbstractBuildin {

	private CdWorker worker;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	CdBuildin(StandardStreams streams) {
		super(streams);
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		this.worker = createChangeToUserHomeWorker();
	}

	private CdWorker createChangeToUserHomeWorker() {
		return new CdWorker() {

			@Override
			public void changeDirectory() {
				Environment e = getEnvironment();
				e.setWorkingDirectory(e.getUserHome());
			}
		};
	}

	@Override
	public ReturnValue call() throws Exception {
		worker.changeDirectory();
		return super.call();
	}

	ReturnValue callBuildin() throws Exception {
		return returnCodeFactory.createSuccess();
	}

	@Override
	protected void setupArguments() throws Exception {
		if (getArgs().length == 1) {
			worker = createChangeDirectoryWorker();
		}
	}

	private CdWorker createChangeDirectoryWorker() {
		return new CdWorker() {

			@Override
			public void changeDirectory() {
				File directory = asFile(getArgs()[0]);
				getEnvironment().setWorkingDirectory(directory);
			}
		};
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		}
		return new File(object.toString());
	}

	/**
	 * Returns the name {@code cd}.
	 */
	@Override
	public String getName() {
		return "cd";
	}
}
