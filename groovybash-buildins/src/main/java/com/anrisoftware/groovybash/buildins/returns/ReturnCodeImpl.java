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
package com.anrisoftware.groovybash.buildins.returns;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.anrisoftware.groovybash.core.ReturnCode;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Sets the return code as a number.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
class ReturnCodeImpl extends Number implements ReturnCode {

	private final Number number;

	/**
	 * Set a code number indicating the success.
	 */
	@AssistedInject
	ReturnCodeImpl() {
		this(0);
	}

	/**
	 * Sets the specified code number.
	 * 
	 * @param number
	 *            the code number.
	 */
	@AssistedInject
	ReturnCodeImpl(@Assisted Number number) {
		this.number = number;
	}

	@Override
	public Number getAsNumber() {
		return number;
	}

	@Override
	public boolean getAsBoolean() {
		return number.intValue() == 0;
	}

	@Override
	public int intValue() {
		return number.intValue();
	}

	@Override
	public long longValue() {
		return number.longValue();
	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}

	@Override
	public float floatValue() {
		return number.floatValue();
	}

	@Override
	public double doubleValue() {
		return number.doubleValue();
	}

	@Override
	public byte byteValue() {
		return number.byteValue();
	}

	@Override
	public short shortValue() {
		return number.shortValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Boolean) {
			return getAsBoolean() == (Boolean) obj;
		}
		if (obj instanceof Number) {
			return getAsNumber().equals(obj);
		}
		if (!(obj instanceof ReturnCode)) {
			return false;
		}
		ReturnCode o = (ReturnCode) obj;
		return new EqualsBuilder().append(number, o.getAsNumber()).isEquals();
	}

	@Override
	public String toString() {
		return number.toString();
	}
}
