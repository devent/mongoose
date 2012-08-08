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
package com.anrisoftware.groovybash.core.buildins.pwdbuildin;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

/**
 * The build-in command {@code pwd}. Prints the absolute path of the current
 * working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PwdBuildin extends AbstractBuildin {

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	PwdBuildin(StandardStreams streams) {
		super(streams);
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		File pwd = getEnvironment().getWorkingDirectory();
		getOutputStream().println(pwd.getAbsolutePath());
		return returnCodeFactory.createSuccess();
	}

	/**
	 * Returns the name {@code pwd}.
	 */
	@Override
	public String getName() {
		return "pwd";
	}
}
