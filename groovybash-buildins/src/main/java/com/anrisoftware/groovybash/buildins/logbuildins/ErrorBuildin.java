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
package com.anrisoftware.groovybash.buildins.logbuildins;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.anrisoftware.groovybash.buildins.StandardStreams;

/**
 * Log a message at the error level of the logger.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ErrorBuildin extends AbstractLogBuildin {

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	ErrorBuildin(StandardStreams streams) {
		super(streams);
	}

	@Override
	protected void logMessage(Logger logger) {
		Object[] arguments = getArguments();
		if (arguments.length > 0 && arguments[0] instanceof Throwable) {
			logger.error(getMessage(), (Throwable) arguments[0]);
		} else {
			logger.error(getMessage(), arguments);
		}
	}

	/**
	 * Returns the name {@code error}.
	 */
	@Override
	public String getName() {
		return "error";
	}
}
