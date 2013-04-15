package com.anrisoftware.mongoose.buildins.listfilesbuildin;

import groovy.lang.Closure;

import java.io.File;
import java.io.FileFilter;

/**
 * Invokes a closure to filter the files.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ClosureFileFilter implements FileFilter {

	private final Closure<?> filter;

	/**
	 * Sets the closure to filter the files.
	 * 
	 * @param filter
	 *            the {@link Closure}.
	 */
	public ClosureFileFilter(Closure<?> filter) {
		this.filter = filter;
	}

	@Override
	public boolean accept(File file) {
		return (Boolean) filter.call(file);
	}

}
