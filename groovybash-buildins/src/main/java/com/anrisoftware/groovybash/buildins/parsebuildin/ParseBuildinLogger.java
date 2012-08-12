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
package com.anrisoftware.groovybash.buildins.parsebuildin;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.join;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ParseBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParseBuildinLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link ParseBuildin}.
	 */
	ParseBuildinLogger() {
		super(ParseBuildin.class);
	}

	void checkMinimumArgs(ParseBuildin buildin, Object[] args) {
		checkArgument(args.length > 0,
				"The build-in %s needs at least one argument.", buildin);
	}

	void errorParseArguments(CmdLineException e, String[] arguments) {
		log.error("Error parse command line arguments ``{}'': {}.",
				join(arguments, ","), e.getLocalizedMessage());
	}
}
