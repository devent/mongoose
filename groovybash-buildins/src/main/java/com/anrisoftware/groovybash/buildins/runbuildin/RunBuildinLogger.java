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
package com.anrisoftware.groovybash.buildins.runbuildin;

import static com.google.common.base.Preconditions.checkElementIndex;
import static java.lang.String.format;

import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link RunBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class RunBuildinLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link RunBuildin}.
	 */
	RunBuildinLogger() {
		super(RunBuildin.class);
	}

	RuntimeException errorReadCommandString(RunBuildin buildin, IOException e) {
		IllegalStateException ex = new IllegalStateException(
				format("Reading from string should always be successfull in the build-in command %s.",
						buildin), e);
		log.error("", ex);
		return ex;
	}

	void checkCommandArgumentIndex(Object[] args) {
		checkElementIndex(0, args.length,
				"Arguments must have the command at index 0.");
	}

	void startingCommand(RunBuildin buildin) {
		log.debug("Starting the command in the build-in command {}...", buildin);
	}

	void commandFinished(RunBuildin buildin) {
		log.info("Finish the command in the build-in command {}...", buildin);
	}

}
