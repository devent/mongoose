package com.anrisoftware.groovybash.core.api;

/**
 * The return value from a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ReturnValue extends Comparable<ReturnValue> {

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
	 * Returns the thrown exception from the command.
	 * 
	 * @return the thrown {@link Exception} or {@code null} if no exception was
	 *         thrown.
	 */
	Exception getException();

	/**
	 * Compare this return value and the specified return value.
	 */
	@Override
	int compareTo(ReturnValue o);

	/**
	 * Compare this return value and a different return value if they are
	 * equals.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a human readable message for this return value.
	 */
	@Override
	String toString();
}
