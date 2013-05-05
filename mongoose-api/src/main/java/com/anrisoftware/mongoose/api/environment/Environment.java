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
package com.anrisoftware.mongoose.api.environment;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * The environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Environment {

	/**
	 * Command line arguments of the script.
	 */
	static final String ARGS_VARIABLE = "ARGS";

	/**
	 * The environment variables of the script.
	 */
	static final String ENV_VARIABLE = "ENV";

	/**
	 * Current working directory.
	 */
	static final String WORKING_DIRECTORY_VARIABLE = "PWD";

	/**
	 * User home directory.
	 */
	static final String HOME_VARIABLE = "HOME";

	/**
	 * User home directory.
	 */
	static final String USER_HOME_VARIABLE = "USER_HOME";

	/**
	 * Directory in which the script was started.
	 */
	static final String SCRIPT_HOME_VARIABLE = "SCRIPT_HOME";

	/**
	 * The script locale.
	 */
	static final String LOCALE_VARIABLE = "LOCALE";

	/**
	 * Texts resources.
	 */
	static final String TEXTS_VARIABLE = "TEXTS";

	/**
	 * Templates resources.
	 */
	static final String TEMPLATES_VARIABLE = "TEMPLATES";

	/**
	 * Commands execution mode {@link ExecutionMode}.
	 */
	static final String EXECUTION_MODE_VARIABLE = "EXECUTION_MODE";

	/**
	 * Sets the environment variables of the script.
	 * 
	 * @param env
	 *            the environment variables {@link Map}.
	 * 
	 * @see System#getenv()
	 */
	void setEnv(Map<String, String> env);

	/**
	 * Returns the environment variables of the script.
	 * 
	 * @return the environment variables {@link Map}.
	 * 
	 * @see System#getenv()
	 */
	Map<String, String> getEnv();

	/**
	 * Sets the command line arguments of the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	void setArgs(List<String> args);

	/**
	 * Returns the command line arguments for the script.
	 * 
	 * @return the command line arguments.
	 */
	List<String> getArgs();

	/**
	 * Tests if the build-in environment have the variable with the specified
	 * name.
	 * 
	 * @param name
	 *            the {@link String} name of the variable.
	 * 
	 * @return {@code true} if the variable exists; {@code false} if not.
	 */
	boolean hasVariable(String name);

	/**
	 * Returns the build-in variable with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the variable.
	 * 
	 * @return the {@link Object} variable.
	 */
	Object getVariable(String name);

	/**
	 * Sets a new value to the build-in variable with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the variable.
	 * 
	 * @param value
	 *            the new {@link Object} value.
	 */
	void setVariable(String name, Object value);

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
	 * Sets the script locale.
	 * 
	 * @param locale
	 *            the {@link Locale}.
	 * 
	 * @throws NullPointerException
	 *             if the specified locale is {@code null}.
	 */
	void setLocale(Locale locale);

	/**
	 * Returns the script locale.
	 * 
	 * @return the {@link Locale}.
	 */
	Locale getLocale();

	/**
	 * Sets the policy how to proceed with commands started in the background.
	 * 
	 * @param policy
	 *            the {@link BackgroundCommandsPolicy} policy.
	 */
	void setBackgroundCommandsPolicy(BackgroundCommandsPolicy policy);

	/**
	 * Returns the policy how to proceed with commands started in the
	 * background.
	 * 
	 * @return the {@link BackgroundCommandsPolicy} policy.
	 */
	BackgroundCommandsPolicy getBackgroundCommandsPolicy();

	/**
	 * Sets the timeout duration for the background commands. After the script
	 * is finished it will wait for background commands to finish before
	 * canceling them.
	 * 
	 * @param duration
	 *            the timeout {@link Duration}.
	 */
	void setBackgroundCommandsTimeout(Duration duration);

	/**
	 * Returns the timeout duration for the background commands.
	 * 
	 * @return the {@link Duration}.
	 */
	Duration getBackgroundCommandsTimeout();

	/**
	 * Sets the commands execution mode.
	 * 
	 * @param mode
	 *            the {@link ExecutionMode}.
	 */
	void setExecutionMode(ExecutionMode mode);

	/**
	 * Returns the commands execution mode.
	 * 
	 * @return the {@link ExecutionMode}.
	 */
	ExecutionMode getExecutionMode();

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
	 * @throws CommandException
	 *             if there was an error executing the command.
	 */
	void executeCommandAndWait(Command command) throws CommandException;

	/**
	 * Shutdown the environment. After shutdown the environment can not execute
	 * any commands.
	 * 
	 * @return a {@link List} of all canceled tasks.
	 * 
	 * @throws InterruptedException
	 *             if interrupted while waiting for the tasks to finish.
	 */
	List<Future<?>> shutdown() throws InterruptedException;

}
