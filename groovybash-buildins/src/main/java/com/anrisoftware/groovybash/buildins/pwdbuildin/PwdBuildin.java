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
package com.anrisoftware.groovybash.buildins.pwdbuildin;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * The build-in command {@code pwd}. Returns the absolute path of the current
 * working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class PwdBuildin extends AbstractBuildin {

	private final PwdValueFactory valueFactory;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	PwdBuildin(StandardStreams streams, PwdValueFactory valueFactory) {
		super(streams);
		this.valueFactory = valueFactory;
	}

	/**
	 * Returns the current working directory.
	 */
	@Override
	public ReturnValue call() throws Exception {
		super.call();
		File pwd = getEnvironment().getWorkingDirectory();
		return valueFactory.create(pwd);
	}

	/**
	 * Returns the name {@code pwd}.
	 */
	@Override
	public String getName() {
		return "pwd";
	}
}
