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

import java.io.File;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;

import com.anrisoftware.mongoose.api.exceptions.ExecutionException;

/**
 * The environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Environment {

	/**
	 * Sets the specified command line arguments for the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	void setArgs(String[] args);

	/**
	 * Returns the specified command line arguments for the script.
	 * 
	 * @return the command line arguments.
	 */
	String[] getArgs();

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

	/**
	 * Returns the environment parameter.
	 * 
	 * @return
	 */
	Map<String, String> getEnv();

	/**
	 * Sets the context for the script logger.
	 * 
	 * @param context
	 *            the {@link Class} context.
	 */
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

	/**
	 * Sets the script home directory.
	 * 
	 * @param dir
	 *            the {@link File} directory.
	 */
	void setScriptHome(File dir);

	/**
	 * Returns the script home directory.
	 * 
	 * @return the {@link File} directory.
	 */
	File getScriptHome();

	/**
	 * Sets the class loader of the script.
	 * 
	 * @param classLoader
	 *            the {@link ClassLoader}.
	 */
	void setScriptClassLoader(ClassLoader classLoader);

	/**
	 * Submit the task for execution in a different thread.
	 * 
	 * @param task
	 *            the {@link Runnable} task.
	 * 
	 * @return the {@link Future} of the task.
	 */
	Future<?> submitTask(Runnable task);

	/**
	 * Execute the command in the environment.
	 * 
	 * @param command
	 *            the {@link Command} to execute.
	 * 
	 * @return the {@link Future} of the command.
	 */
	Future<Command> executeCommand(Command command);

	/**
	 * Execute the command in the environment and wait for the command to
	 * finish.
	 * 
	 * @param command
	 *            the {@link Command} to execute.
	 * 
	 * @throws ExecutionException
	 *             if the command returns with an error.
	 */
	void executeCommandAndWait(Command command) throws ExecutionException;

	/**
	 * Close the environment.
	 */
	void close();

}
