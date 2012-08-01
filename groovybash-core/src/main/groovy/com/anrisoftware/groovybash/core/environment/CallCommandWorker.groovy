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
package com.anrisoftware.groovybash.core.environment

import javax.inject.Inject

/**
 * Call the command. The worker is used because 
 * {@link GroovyObject#invokeMethod(String, Object)} cannot throw any 
 * checked exceptions, so we use Groovy dynamic nature to throw a checked
 * exception regardless.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CallCommandWorker {

	private final CallCommandWorkerLogger log

	@Inject
	CallCommandWorker(CallCommandWorkerLogger log) {
		this.log = log
	}

	/**
	 * Call the specified command, log and re-throw any exceptions.
	 * 
	 * @param command
	 * 			  the command to call.
	 * 
	 * @return the return value of the command.
	 */
	def call(def command) {
		try {
			command.call()
		} catch (Exception e) {
			log.errorCallBuildin(command, e)
			throw e
		}
	}
}
