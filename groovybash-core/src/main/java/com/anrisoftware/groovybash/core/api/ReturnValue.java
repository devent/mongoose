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

import java.io.InputStream;
import java.io.PrintStream;

/**
 * The return value from a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ReturnValue extends Comparable<ReturnValue> {

	/**
	 * Returns the standard input stream of the command.
	 * 
	 * @return the {@link InputStream} for standard input.
	 */
	InputStream getInputStream();

	/**
	 * Returns the standard output stream of the command.
	 * 
	 * @return the {@link PrintStream} for standard output.
	 */
	PrintStream getOutputStream();

	/**
	 * Returns the standard error stream of the command.
	 * 
	 * @return the {@link PrintStream} for standard errors.
	 */
	PrintStream getErrorStream();

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
	 * Compare this return value and the specified return value.
	 */
	@Override
	int compareTo(ReturnValue o);

	/**
	 * Compare this return value and a different return value if they are
	 * equals. Two return values are equals if they represent the same number
	 * value.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a human readable message for this return value.
	 */
	@Override
	String toString();
}
