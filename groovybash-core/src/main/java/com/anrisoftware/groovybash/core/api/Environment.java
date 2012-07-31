package com.anrisoftware.groovybash.core.api;

import java.io.File;

import com.google.inject.Injector;

/**
 * The environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Environment {

	/**
	 * Sets the parent injector for the environment.
	 * 
	 * @param injector
	 *            the parent {@link Injector}.
	 */
	void setInjector(Injector injector);

	/**
	 * Sets the current working directory.
	 * 
	 * @param directory
	 *            the {@link File} of the directory.
	 */
	void setWorkingDirectory(File directory);

	/**
	 * Returns the current working directory.
	 * 
	 * @return the {@link File} of the directory.
	 */
	File getWorkingDirectory();

}
