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

import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import com.anrisoftware.mongoose.api.exceptions.ExecutionException;

/**
 * Command that can be executed.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Command extends Callable<Command> {

	/**
	 * Property for the output target object.
	 */
	static final String OUTPUT_TARGET_PROPERTY = "output_target";

	/**
	 * Property for the error target object.
	 */
	static final String ERROR_TARGET_PROPERTY = "error_target";

	/**
	 * Property for the input source object.
	 */
	static final String INPUT_SOURCE_PROPERTY = "input_source";

	/**
	 * Property for the command arguments.
	 */
	static final String ARGUMENTS_PROPERTY = "arguments";

	/**
	 * Sets the environment of the command. The environment contains the global
	 * variables.
	 * 
	 * @param environment
	 *            the {@link Environment}.
	 */
	void setEnvironment(Environment environment);

	/**
	 * Returns the environment of the command. The environment contains the
	 * global variables.
	 * 
	 * @return the {@link Environment}.
	 */
	Environment getEnvironment();

	/**
	 * Returns the name of the command.
	 * 
	 * @return the name of the command.
	 */
	String getName();

	/**
	 * Executes the command and waits until the command is finished.
	 * 
	 * @return the {@link Command} that was executed.
	 * 
	 * @throws ExecutionException
	 *             if there was an error executing the command.
	 */
	@Override
	Command call() throws ExecutionException;

	/**
	 * @see #call()
	 * @see #setArgs(Object)
	 * 
	 * @param args
	 *            the arguments.
	 * 
	 */
	Command call(Object args) throws Exception;

	/**
	 * Sets the arguments for the command.
	 * 
	 * @param args
	 *            the arguments.
	 * 
	 * @throws NullPointerException
	 *             if the specified arguments are {@code null}.
	 * 
	 * @throws Exception
	 *             if some errors are encountered.
	 * 
	 * @see #ARGUMENTS_PROPERTY
	 */
	void setArgs(Object args) throws Exception;

	/**
	 * @see #setArgs(Object)
	 * 
	 * @return this {@link Command}.
	 */
	Command args(Object args) throws Exception;

	/**
	 * Returns the arguments of the command.
	 * 
	 * @return the arguments.
	 */
	Map<Object, Object> getArgs();

	/**
	 * Sets the stream that is connected with the standard input of the command.
	 * 
	 * @param stream
	 *            the {@link OutputStream} for standard input.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	void setInputStream(OutputStream stream);

	/**
	 * Returns the stream that is connected with the standard input of the
	 * command.
	 * 
	 * @return the {@link OutputStream} for standard input.
	 */
	OutputStream getInputStream();

	/**
	 * Sets the specified file name as the data source for the input of the
	 * command.
	 * <dl>
	 * <dt>{@link InputStream}</dt>
	 * <dd>the specified stream is set as the data source for the input of the
	 * command.</dd>
	 * 
	 * <dt>{@link File}</dt>
	 * <dd>the specified file is set as the data source for the input of the
	 * command.</dd>
	 * 
	 * <dt>{@link Object}</dt>
	 * <dd>the string representation of the object is used as the file name.</dd>
	 * </dl>
	 * 
	 * @param source
	 *            the source {@link Object}.
	 * 
	 * @throws NullPointerException
	 *             if the specified source is {@code null}.
	 * 
	 * @throws Exception
	 *             if the file could not be found.
	 * 
	 * @see #INPUT_SOURCE_PROPERTY
	 */
	void setInput(Object source) throws Exception;

	/**
	 * @see #setInput(Object)
	 * 
	 * @return this command.
	 */
	Command input(Object source) throws Exception;

	/**
	 * Sets the stream that is connected with the standard output of the
	 * command.
	 * 
	 * @param stream
	 *            the {@link InputStream} for standard output.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	void setOutputStream(InputStream stream);

	/**
	 * Returns the stream that is connected with the standard output of the
	 * command.
	 * 
	 * @return the {@link InputStream} for standard output.
	 */
	InputStream getOutputStream();

	/**
	 * Sets the specified target as the data target for the output of the
	 * command.
	 * <dl>
	 * <dt>{@link OutputStream}</dt>
	 * <dd>the specified stream is set as the target for the output of the
	 * command.</dd>
	 * 
	 * <dt>{@link File}</dt>
	 * <dd>the specified file is set as the target for the output of the
	 * command.</dd>
	 * 
	 * <dt>{@link Object}</dt>
	 * <dd>the string representation of the object is used as the file name.</dd>
	 * </dl>
	 * 
	 * @param target
	 *            the target {@link Object}.
	 * 
	 * @throws NullPointerException
	 *             if the specified object is {@code null}.
	 * 
	 * @throws Exception
	 *             if the file could not be found.
	 * 
	 * @see #OUTPUT_TARGET_PROPERTY
	 */
	void setOutput(Object target) throws Exception;

	/**
	 * @see #setOutput(Object)
	 * 
	 * @param append
	 *            set to {@code true} to append the file instead of overriding.
	 */
	void setOutput(Object target, boolean append) throws Exception;

	/**
	 * @see #setOutput(Object, boolean)
	 * 
	 * @param descriptor
	 *            set the file descriptor number.
	 */
	void setOutput(int descriptor, Object target, boolean append)
			throws Exception;

	/**
	 * @see #setOutput(Object)
	 * 
	 * @return this command.
	 */
	Command output(Object target) throws Exception;

	/**
	 * Sets the stream that is connected with the standard error output of the
	 * command.
	 * 
	 * @param stream
	 *            the {@link InputStream} for the standard errors.
	 * 
	 * @throws NullPointerException
	 *             if the specified stream is {@code null}.
	 */
	void setErrorStream(InputStream stream);

	/**
	 * Returns the stream that is connected with the standard error output of
	 * the command.
	 * 
	 * @return the {@link InputStream} for standard errors.
	 */
	InputStream getErrorStream();

	/**
	 * Sets the specified target as the data target for the error output of the
	 * command.
	 * <dl>
	 * <dt>{@link OutputStream}</dt>
	 * <dd>the specified stream is set as the target for the error output of the
	 * command.</dd>
	 * 
	 * <dt>{@link File}</dt>
	 * <dd>the specified file is set as the target for the error output of the
	 * command.</dd>
	 * 
	 * <dt>{@link Object}</dt>
	 * <dd>the string representation of the object is used as the file name.</dd>
	 * </dl>
	 * 
	 * @param target
	 *            the target {@link Object}.
	 * 
	 * @throws NullPointerException
	 *             if the specified object is {@code null}.
	 * 
	 * @throws Exception
	 *             if the file could not be found.
	 * 
	 * @see #ERROR_TARGET_PROPERTY
	 */
	void setError(Object target) throws Exception;

	/**
	 * @see #setError(Object)
	 * 
	 * @param append
	 *            set to {@code true} to append the file instead of overriding.
	 */
	void setError(Object obj, boolean append) throws Exception;

	/**
	 * @see #setError(Object)
	 * 
	 * @return this command.
	 */
	Command error(Object target) throws Exception;

	/**
	 * @see VetoableChangeSupport#addVetoableChangeListener(VetoableChangeListener)
	 * @see #OUTPUT_TARGET_PROPERTY
	 * @see #ERROR_TARGET_PROPERTY
	 * @see #INPUT_SOURCE_PROPERTY
	 * @see #ARGUMENTS_PROPERTY
	 */
	void addVetoableChangeListener(VetoableChangeListener listener);

	/**
	 * @see VetoableChangeSupport#removeVetoableChangeListener(VetoableChangeListener)
	 * @see #OUTPUT_TARGET_PROPERTY
	 * @see #ERROR_TARGET_PROPERTY
	 * @see #INPUT_SOURCE_PROPERTY
	 * @see #ARGUMENTS_PROPERTY
	 */
	void removeVetoableChangeListener(VetoableChangeListener listener);

	/**
	 * @see VetoableChangeSupport#addVetoableChangeListener(String,
	 *      VetoableChangeListener)
	 * @see #OUTPUT_TARGET_PROPERTY
	 * @see #ERROR_TARGET_PROPERTY
	 * @see #INPUT_SOURCE_PROPERTY
	 * @see #ARGUMENTS_PROPERTY
	 */
	void addVetoableChangeListener(String propertyName,
			VetoableChangeListener listener);

	/**
	 * @see VetoableChangeSupport#removeVetoableChangeListener(String,
	 *      VetoableChangeListener)
	 * @see #OUTPUT_TARGET_PROPERTY
	 * @see #ERROR_TARGET_PROPERTY
	 * @see #INPUT_SOURCE_PROPERTY
	 * @see #ARGUMENTS_PROPERTY
	 */
	void removeVetoableChangeListener(String propertyName,
			VetoableChangeListener listener);
}
