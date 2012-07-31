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
