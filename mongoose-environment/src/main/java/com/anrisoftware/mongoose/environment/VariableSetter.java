package com.anrisoftware.mongoose.environment;

/**
 * Sets a build-in variable.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface VariableSetter {

	/**
	 * Sets a build-in variable to the new value.
	 * 
	 * @param value
	 *            the new {@link Object} value.
	 */
	void setVariable(Object value);
}
