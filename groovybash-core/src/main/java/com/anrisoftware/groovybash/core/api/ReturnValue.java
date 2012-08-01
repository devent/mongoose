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
	 * equals. Two return values are equals if they represent the same exception
	 * or they have the same number value.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a human readable message for this return value.
	 */
	@Override
	String toString();
}
