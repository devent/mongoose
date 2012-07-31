package com.anrisoftware.groovybash.core.buildins;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.anrisoftware.groovybash.core.api.ReturnValue;

/**
 * Default return value.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DefaultReturnValue implements ReturnValue {

	public static final ReturnValue SUCCESS_VALUE = new DefaultReturnValue(true);

	private final Number number;

	private final Exception exception;

	public DefaultReturnValue(boolean bool) {
		this(bool, null);
	}

	public DefaultReturnValue(boolean bool, Exception exception) {
		this(bool ? 0 : 1, exception);
	}

	public DefaultReturnValue(Number number) {
		this(number, null);
	}

	public DefaultReturnValue(Number number, Exception exception) {
		this.number = number;
		this.exception = exception;
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
	public Exception getException() {
		return exception;
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
		if (exception != null || o.getException() != null) {
			return new EqualsBuilder().append(exception, o.getException())
					.isEquals();
		} else {
			return new EqualsBuilder().append(number, o.getAsNumber())
					.isEquals();
		}
	}

	@Override
	public int compareTo(ReturnValue o) {
		if (exception != null || o.getException() != null) {
			return new CompareToBuilder().append(exception, o.getException())
					.toComparison();
		} else {
			return new CompareToBuilder().append(number, o.getAsNumber())
					.toComparison();
		}
	}

	@Override
	public String toString() {
		if (exception != null) {
			return exception.getLocalizedMessage();
		} else {
			return number.toString();
		}
	}
}
