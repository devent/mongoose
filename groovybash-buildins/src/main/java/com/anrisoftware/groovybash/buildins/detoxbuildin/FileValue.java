package com.anrisoftware.groovybash.buildins.detoxbuildin;

import static java.lang.String.format;

import java.io.File;

import javax.annotation.Nullable;
import javax.inject.Inject;

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
