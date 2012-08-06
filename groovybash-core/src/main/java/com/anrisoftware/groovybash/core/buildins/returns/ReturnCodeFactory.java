package com.anrisoftware.groovybash.core.buildins.returns;

import com.anrisoftware.groovybash.core.api.ReturnCode;

/**
 * Factory to create a new return code.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ReturnCodeFactory {

	/**
	 * Creates a new return code indicating the success.
	 * 
	 * @return the {@link ReturnCode}.
	 */
	ReturnCode createSuccess();

	/**
	 * Creates a new return code with the specified number.
	 * 
	 * @param number
	 *            the code number.
	 * 
	 * @return the {@link ReturnCode}.
	 */
	ReturnCode create(Number number);
}
