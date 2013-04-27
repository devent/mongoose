package com.anrisoftware.mongoose.api.commans;

/**
 * Policy how to proceed with commands started in the background.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BackgroundCommandsPolicy {

	/**
	 * Wait for all background commands to finish.
	 */
	WAIT,

	/**
	 * Wait for all background commands to finish with an infinite timeout.
	 */
	WAIT_NO_TIMEOUT,

	/**
	 * Cancel all background commands.
	 */
	CANCEL
}
