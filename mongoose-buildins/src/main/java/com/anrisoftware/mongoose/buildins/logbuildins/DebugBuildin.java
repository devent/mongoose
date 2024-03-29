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
package com.anrisoftware.mongoose.buildins.logbuildins;

import org.slf4j.Logger;

/**
 * Log a message at the debug level of the logger.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DebugBuildin extends AbstractLogBuildin {

	/**
	 * Returns the name {@code debug}.
	 */
	@Override
	public String getTheName() {
		return "debug";
	}

	@Override
	protected void logMessage(Logger logger) {
		logger.debug(getMessage(), getArguments());
	}

	@Override
	public boolean getIsEnabled() {
		return getTheEnvironment().getScriptLogger().isDebugEnabled();
	}
}
