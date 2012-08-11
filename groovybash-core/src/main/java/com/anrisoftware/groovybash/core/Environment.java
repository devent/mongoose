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
package com.anrisoftware.groovybash.core;

import java.io.File;
import java.util.concurrent.Future;

import org.slf4j.Logger;

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
	 * Sets the specified command line arguments for the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	void setArguments(String[] args);

	/**
	 * Returns the specified command line arguments for the script.
	 * 
	 * @return the command line arguments.
	 */
	String[] getArguments();

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

	void setScriptLoggerContext(Class<?> context);

	/**
	 * Returns the script logger.
	 * 
	 * @return the {@link Logger}.
	 */
	Logger getScriptLogger();

	/**
	 * Returns the user home directory.
	 * 
	 * @return the user home directory {@link File}.
	 * @see System#getProperty(String)
	 */
	File getUserHome();

	Future<?> submitTask(Runnable task);

	/**
	 * Close the environment.
	 */
	void close();

}
