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
package com.anrisoftware.mongoose.api.commans;

/**
 * The numerical return code of a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ReturnCode extends ReturnValue {

	/**
	 * Returns the number value of the return value.
	 * 
	 * @return the {@link Number} value.
	 */
	Number getAsNumber();

	/**
	 * Returns the boolean value of the return value. As a convention the return
	 * value {@code 0} equals {@code true} and any value not {@code 0} equals
	 * {@code false}.
	 * 
	 * @return the boolean value.
	 */
	boolean getAsBoolean();

	/**
	 * Compare this return value and a different return value if they are
	 * equals.
	 * 
	 * @return {@code true} if one of the following conditions apply:
	 *         <dl>
	 *         <dt>{@code obj} is of type {@code Boolean}:</dt>
	 *         <dd>if this value as boolean equals to the specified boolean
	 *         value.</dd>
	 *         <dt>{@code obj} is of type {@code Number}:</dt>
	 *         <dd>if this value as number equals to the specified number value.
	 *         </dd>
	 *         <dt>{@code obj} is of type {@code ReturnCode}:</dt>
	 *         <dd>if this value as number equals to the specified return code
	 *         number value.</dd>
	 *         </dl>
	 */
	@Override
	boolean equals(Object obj);

}
