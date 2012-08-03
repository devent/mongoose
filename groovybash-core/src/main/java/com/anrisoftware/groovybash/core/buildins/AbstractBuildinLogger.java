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
package com.anrisoftware.groovybash.core.buildins;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AbstractBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractBuildinLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link AbstractBuildin}.
	 */
	AbstractBuildinLogger() {
		super(AbstractBuildin.class);
	}

	void outputFileSet(AbstractBuildin buildin, File file) {
		log.debug("Set output file {} for the build-in command {}.", file,
				buildin);
	}

	void outputStreamSet(AbstractBuildin buildin, OutputStream stream) {
		log.debug("Set output stream {} for the build-in command {}.", stream,
				buildin);
	}

	void inputFileSet(AbstractBuildin buildin, File file) {
		log.debug("Set input file {} for the build-in command {}.", file,
				buildin);
	}

	void inputStreamSet(AbstractBuildin buildin, InputStream stream) {
		log.debug("Set input stream {} for the build-in command {}.", stream,
				buildin);
	}
}
