package com.anrisoftware.groovybash.core;

public interface ReturnCode extends ReturnValue {

	/**
	 * Returns the number value of the return value.
	 * 
	 * @return the {@link Number} value.
	 */
	Number getAsNumber();

	/**
	 * Returns the boolean value of the return value.
	 * 
	 * @return the boolean value.
	 */
	boolean getAsBoolean();

	/**
	 * Compare this return value and a different return value if they are
	 * equals. Two return values are equals if they represent the same number
	 * value.
	 */
	@Override
	boolean equals(Object obj);

}
