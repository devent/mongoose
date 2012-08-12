/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.buildins.pwdbuildin;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns the current working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class PwdReturnValue implements ReturnValue {

	private final File directory;

	/**
	 * Sets the current working directory.
	 * 
	 * @param directory
	 *            the current working {@link File} directory.
	 */
	@Inject
	PwdReturnValue(@Assisted File directory) {
		this.directory = directory;
	}

	/**
	 * Returns the current working directory.
	 * 
	 * @return the {@link File} directory.
	 */
	public File getDir() {
		return directory;
	}

	/**
	 * @return {@code true} if the object is a {@link File} and equals to the
	 *         current working directory; if the object is
	 *         {@link PwdReturnValue} and the current working directory equals
	 *         this working directory.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof File) {
			File expecting = (File) obj;
			return directory.equals(expecting);
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		PwdReturnValue rhs = (PwdReturnValue) obj;
		return new EqualsBuilder().append(directory, rhs.directory).isEquals();

	}

	/**
	 * @return the absolute path of the current working directory.
	 */
	@Override
	public String toString() {
		return directory.getAbsolutePath();
	}
}
