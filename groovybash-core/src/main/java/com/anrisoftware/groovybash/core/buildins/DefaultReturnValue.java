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
package com.anrisoftware.groovybash.core.buildins;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.anrisoftware.groovybash.core.api.ReturnValue;

/**
 * Default return value.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DefaultReturnValue extends Number implements ReturnValue {

	public static ReturnValue createSuccessValue(InputStream inputStream,
			PrintStream outputStream, PrintStream errorStream) {
		return new DefaultReturnValue(true, inputStream, outputStream,
				errorStream);
	}

	private final Number number;

	private final InputStream inputStream;

	private final PrintStream outputStream;

	private final PrintStream errorStream;

	public DefaultReturnValue(boolean bool, InputStream inputStream,
			PrintStream outputStream, PrintStream errorStream) {
		this(bool ? 0 : 1, inputStream, outputStream, errorStream);
	}

	public DefaultReturnValue(Number number, InputStream inputStream,
			PrintStream outputStream, PrintStream errorStream) {
		this.number = number;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.errorStream = errorStream;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public PrintStream getOutputStream() {
		return outputStream;
	}

	@Override
	public PrintStream getErrorStream() {
		return errorStream;
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
		if (!(obj instanceof ReturnValue)) {
			return false;
		}
		ReturnValue o = (ReturnValue) obj;
		return new EqualsBuilder().append(number, o.getAsNumber()).isEquals();
	}

	@Override
	public int compareTo(ReturnValue o) {
		return new CompareToBuilder().append(number, o.getAsNumber())
				.toComparison();
	}

	@Override
	public String toString() {
		return number.toString();
	}
}
