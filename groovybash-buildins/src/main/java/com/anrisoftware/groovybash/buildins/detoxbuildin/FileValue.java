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
package com.anrisoftware.groovybash.buildins.detoxbuildin;

import static java.lang.String.format;

import java.io.File;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.groovybash.core.ReturnCode;
import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.inject.assistedinject.Assisted;

/**
 * Value for a file name.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class FileValue implements ReturnValue {

	private final File file;

	/**
	 * Sets the file name with the optional parent path.
	 * 
	 * @param parentFile
	 *            the parent {@link File} path, can be {@code null}.
	 * 
	 * @param name
	 *            the file name.
	 */
	@Inject
	FileValue(@Assisted @Nullable File parentFile, @Assisted String name) {
		this.file = new File(parentFile, name);
	}

	/**
	 * Returns the file.
	 * 
	 * @return the {@link File}.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns this value as the specified type.
	 * 
	 * @param type
	 *            the {@link Class} type. The type can be of class:
	 *            <dl>
	 *            <dt>{@link String}</dt>
	 *            <dd>returns the absolute path of the file.</dd>
	 *            <dt>{@link File}</dt>
	 *            <dd>returns the file.</dd>
	 *            </dl>
	 * 
	 * @return this value as the specified type.
	 * 
	 * @throws ClassCastException
	 *             if we can not convert this value to the specified type.
	 */
	public Object asType(Class<?> type) {
		if (type == String.class) {
			return file.getAbsolutePath();
		}
		if (type == File.class) {
			return file;
		}
		throw new ClassCastException(format("Can not convert %s as type %s.",
				getClass(), type));
	}

	@Override
	public String toString() {
		return file.toString();
	}

	/**
	 * Delegate missing methods to the file that this value is representing.
	 * 
	 * @param name
	 *            the name of the method.
	 * 
	 * @param args
	 *            the arguments of the method.
	 * 
	 * @return the return value of the {@link File} method.
	 */
	public Object methodMissing(String name, Object args) {
		return InvokerHelper.invokeMethod(file, name, args);
	}

	/**
	 * Delegate missing properties to the file that this value is representing.
	 * 
	 * @param name
	 *            the name of the property.
	 * 
	 * @return the value of the property from the {@link File}.
	 */
	public Object propertyMissing(String name) {
		return InvokerHelper.getProperty(file, name);
	}

	/**
	 * Compare this return value and a different return value if they are
	 * equals.
	 * 
	 * @return {@code true} if one of the following conditions apply:
	 *         <dl>
	 *         <dt>{@code obj} is of type {@link Object}:</dt>
	 *         <dd>if this file equals to the string representation of the
	 *         object interpreted as a file path.</dd>
	 *         <dt>{@code obj} is of type {@link File}:</dt>
	 *         <dd>if this file equals to the specified file.
	 *         <dt>{@code obj} is of type {@link FileValue}:</dt>
	 *         <dd>if this file equals to the specified value file.</dd>
	 *         </dl>
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
			File rhsFile = (File) obj;
			return file.equals(rhsFile);
		}
		if ((obj instanceof ReturnCode)) {
			FileValue o = (FileValue) obj;
			return file.equals(o.getFile());
		}
		File rhsFile = new File(obj.toString());
		return file.equals(rhsFile);
	}
}
