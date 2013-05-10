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
package com.anrisoftware.mongoose.buildins.sudobuildin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * The build-in command {@code sudo}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SudoBuildin extends AbstractCommand {

	private final SudoBuildinLogger log;

	private Command command;

	private Backend backend;

	private final VetoableChangeListener commandListener;

	/**
	 * @param logger
	 *            the {@link SudoBuildinLogger} for logging messages;
	 */
	@Inject
	SudoBuildin(SudoBuildinLogger logger,
			@Named("sudo-default-backend") Backend backend) {
		this.log = logger;
		this.backend = backend;
		this.commandListener = new VetoableChangeListener() {

			@Override
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				try {
					setupChangedProperties(evt);
				} catch (Exception e) {
					throw log.errorSetupProperties(SudoBuildin.this, e, evt);
				}
			}
		};
	}

	private void setupChangedProperties(PropertyChangeEvent evt)
			throws Exception {
		if (evt.getPropertyName() == OUTPUT_TARGET_PROPERTY) {
			command.setOutput(evt.getNewValue());
			return;
		}
		if (evt.getPropertyName() == ERROR_TARGET_PROPERTY) {
			command.setError(evt.getNewValue());
			return;
		}
		if (evt.getPropertyName() == INPUT_SOURCE_PROPERTY) {
			command.setInput(evt.getNewValue());
			return;
		}
	}

	@Override
	protected void doCall() throws Exception {
		command = backend.getBackendCommand(getArgs(), getUnnamedArgs());
		command.setEnvironment(getTheEnvironment());
		command.setInput(getInput());
		command.setOutput(getOutput());
		command.setError(getError());
		command.addVetoableChangeListener(commandListener);
		getTheEnvironment().executeCommandAndWait(command);
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		if (args.containsKey("backend")) {
			setBackend(args.get("backend"));
		}
	}

	/**
	 * Sets the {@code sudo} back-end to execute commands with root privileges.
	 * 
	 * @param object
	 *            either the {@link Backend} or a {@link Class}; the class type
	 *            is instantiated with the default constructor and set as the
	 *            handler.
	 * 
	 * @throws CommandException
	 *             if the specified object is not an {@link Backend}; if there
	 *             were errors instantiate the class type.
	 */
	public void setBackend(Object object) throws CommandException {
		if (object instanceof Class) {
			@SuppressWarnings("unchecked")
			Class<Backend> type = (Class<Backend>) object;
			this.backend = createType(type);
		} else if (object instanceof Backend) {
			this.backend = (Backend) object;
		} else {
			throw log.errorBackendType(this, object);
		}
		log.backendSet(this, object);
	}

	/**
	 * Delegates missing methods to the {@code exec} command.
	 */
	public Object methodMissing(String name, Object args) {
		return InvokerHelper.invokeMethod(command, name, args);
	}

	/**
	 * Sets property values to the {@code exec} command's properties.
	 */
	public void propertyMissing(String name, Object value) {
		InvokerHelper.setProperty(command, name, value);
	}

	/**
	 * Returns property values from the {@code exec} command.
	 */
	public Object propertyMissing(String name) {
		return InvokerHelper.getProperty(command, name);
	}

	/**
	 * Returns the name {@code sudo}.
	 */
	@Override
	public String getTheName() {
		return SudoService.ID;
	}
}
