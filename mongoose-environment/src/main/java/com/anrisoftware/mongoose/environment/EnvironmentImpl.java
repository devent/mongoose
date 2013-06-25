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
package com.anrisoftware.mongoose.environment;

import static com.anrisoftware.mongoose.api.environment.BackgroundCommandsPolicy.POLICY_FORMAT;
import static com.anrisoftware.mongoose.resources.LocaleHooks.DISPLAY_LOCALE_PROPERTY;
import static java.util.Collections.unmodifiableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.globalpom.threads.api.ListenableFuture;
import com.anrisoftware.globalpom.threads.api.ThreadsException;
import com.anrisoftware.globalpom.threads.properties.PropertiesThreads;
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory;
import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.anrisoftware.mongoose.api.environment.BackgroundCommandsPolicy;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.environment.ExecutionMode;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.resources.LocaleHooks;
import com.anrisoftware.mongoose.resources.TemplatesResources;
import com.anrisoftware.mongoose.resources.TextsResources;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Provides the environment variables and loads commands as Java service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EnvironmentImpl implements Environment {

	private static final String EXEC_COMMAND = "exec";

	private static final String USER_HOME_PROPERTY = System
			.getProperty("user.home");

	private static final String EXECUTION_MODE_PROPERTY = "execution_mode";

	private static final String BACKGROUND_COMMANDS_TIMEOUT_PROPERTY = "background_commands_timeout";

	private static final String BACKGROUND_COMMANDS_POLICY_PROPERTY = "background_commands_policy";

	private final EnvironmentImplLogger log;

	private final PropertiesThreads threads;

	private final Map<String, Object> variables;

	private final TextsResources textsResources;

	private final TemplatesResources templatesResources;

	private final Map<String, VariableSetter> variablesSetters;

	private Logger scriptLogger;

	private BackgroundCommandsPolicy backgroundCommandsPolicy;

	private Duration backgroundCommandsTimeout;

	@Inject
	EnvironmentImpl(EnvironmentImplLogger logger,
			@Named("threads-properties") Properties threadsProperties,
			@Named("environment-properties") ContextProperties properties,
			PropertiesThreadsFactory threadsFactory,
			TextsResources textsResources,
			TemplatesResources templatesResources, LocaleHooks localeHooks)
			throws ParseException, ThreadsException {
		this.log = logger;
		this.threads = threadsFactory.create(threadsProperties, "script");
		this.variables = new HashMap<String, Object>();
		this.variablesSetters = new HashMap<String, VariableSetter>();
		this.textsResources = textsResources;
		this.templatesResources = templatesResources;
		this.backgroundCommandsPolicy = properties.getTypedProperty(
				BACKGROUND_COMMANDS_POLICY_PROPERTY, POLICY_FORMAT);
		this.backgroundCommandsTimeout = Duration.parse(properties
				.getProperty(BACKGROUND_COMMANDS_TIMEOUT_PROPERTY));
		setExecutionMode(properties.<ExecutionMode> getTypedProperty(
				EXECUTION_MODE_PROPERTY, ExecutionMode.FORMAT));
		setupLocaleHooks(localeHooks);
		setupVariables();
		setupVariablesSetters();
	}

	private void setupLocaleHooks(LocaleHooks localeHooks) {
		localeHooks.addPropertyChangeListener(DISPLAY_LOCALE_PROPERTY,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						setLocale((Locale) evt.getNewValue());
					}
				});
		localeHooks.hookDefaultLocale();
	}

	private void setupVariables() {
		setEnv(System.getenv());
		setArgs(Collections.<String> emptyList());
		setScriptHome(new File("."));
		setWorkingDirectory(new File("."));
		setHomeDirectory();
		setResources();
		setLocale(Locale.getDefault());
	}

	private void setupVariablesSetters() {
		variablesSetters.put(ARGS_VARIABLE, new VariableSetter() {

			@SuppressWarnings("unchecked")
			@Override
			public void setVariable(Object value) {
				setArgs((List<String>) value);
			}
		});
		variablesSetters.put(EXECUTION_MODE_VARIABLE, new VariableSetter() {

			@Override
			public void setVariable(Object value) {
				setExecutionMode((ExecutionMode) value);
			}
		});
		variablesSetters.put(LOCALE_VARIABLE, new VariableSetter() {

			@Override
			public void setVariable(Object value) {
				setLocale((Locale) value);
			}
		});
		variablesSetters.put(WORKING_DIRECTORY_VARIABLE, new VariableSetter() {

			@Override
			public void setVariable(Object value) {
				setWorkingDirectory((File) value);
			}
		});
		variablesSetters.put(ENV_VARIABLE, new VariableSetter() {

			@SuppressWarnings("unchecked")
			@Override
			public void setVariable(Object value) {
				setEnv((Map<String, String>) value);
			}
		});
	}

	private void setHomeDirectory() {
		variables.put(USER_HOME_VARIABLE, new File(USER_HOME_PROPERTY));
		variables.put(HOME_VARIABLE, new File(USER_HOME_PROPERTY));
	}

	private void setResources() {
		variables.put(TEXTS_VARIABLE, textsResources);
		variables.put(TEMPLATES_VARIABLE, templatesResources);
	}

	@Override
	public void setEnv(Map<String, String> env) {
		HashMap<String, String> value = new HashMap<String, String>(env);
		variables.put(ENV_VARIABLE, value);
		log.environmentSet(this, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getEnv() {
		return new HashMap<String, String>(
				(Map<String, String>) variables.get(ENV_VARIABLE));
	}

	@Override
	public void setArgs(List<String> args) {
		variables.put(ARGS_VARIABLE, copyArgs(args));
		log.argumentsSet(args);
	}

	private List<String> copyArgs(List<?> args) {
		List<String> list = new ArrayList<String>();
		for (Object string : args) {
			list.add(string.toString());
		}
		return unmodifiableList(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getArgs() {
		return (List<String>) variables.get(ARGS_VARIABLE);
	}

	@Override
	public void setWorkingDirectory(File directory) {
		variables.put(WORKING_DIRECTORY_VARIABLE, directory);
		log.workingDirectorySet(directory);
	}

	@Override
	public File getWorkingDirectory() {
		return (File) variables.get(WORKING_DIRECTORY_VARIABLE);
	}

	@Override
	public File getUserHome() {
		return (File) variables.get(HOME_VARIABLE);
	}

	@Override
	public void setScriptHome(File dir) {
		variables.put(SCRIPT_HOME_VARIABLE, dir);
		log.scriptHomeSet(dir);
	}

	@Override
	public File getScriptHome() {
		return (File) variables.get(SCRIPT_HOME_VARIABLE);
	}

	@Override
	public void setScriptLoggerContext(Class<?> context) {
		scriptLogger = LoggerFactory.getLogger(context);
	}

	@Override
	public Logger getScriptLogger() {
		return scriptLogger;
	}

	@Override
	public void setScriptClassLoader(ClassLoader classLoader) {
		textsResources.setClassLoader(classLoader);
		templatesResources.setClassLoader(classLoader);
	}

	@Override
	public void setLocale(Locale locale) {
		log.checkLocale(this, locale);
		variables.put(LOCALE_VARIABLE, locale);
		textsResources.setLocale(locale);
		templatesResources.setLocale(locale);
		log.localeSet(this, locale);
	}

	@Override
	public Locale getLocale() {
		return (Locale) variables.get(LOCALE_VARIABLE);
	}

	@Override
	public void setBackgroundCommandsPolicy(BackgroundCommandsPolicy policy) {
		this.backgroundCommandsPolicy = policy;
	}

	@Override
	public BackgroundCommandsPolicy getBackgroundCommandsPolicy() {
		return backgroundCommandsPolicy;
	}

	@Override
	public void setBackgroundCommandsTimeout(Duration duration) {
		this.backgroundCommandsTimeout = duration;
	}

	@Override
	public Duration getBackgroundCommandsTimeout() {
		return backgroundCommandsTimeout;
	}

	@Override
	public void setExecutionMode(ExecutionMode mode) {
		variables.put(EXECUTION_MODE_VARIABLE, mode);
		log.executionModeSet(this, mode);
	}

	@Override
	public ExecutionMode getExecutionMode() {
		return (ExecutionMode) variables.get(EXECUTION_MODE_VARIABLE);
	}

	@Override
	public void setVariable(String name, Object value) {
		log.checkVariable(hasVariable(name), name);
		log.checkVariableReadOnly(variablesSetters.containsKey(name), name);
		variablesSetters.get(name).setVariable(value);
	}

	@Override
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	@Override
	public Object getVariable(String name) {
		log.checkVariable(hasVariable(name), name);
		return variables.get(name);
	}

	/**
	 * Returns the command if it is not a method of the environment.
	 * 
	 * @throws Exception
	 *             if some errors are encountered from the command.
	 */
	public Object methodMissing(String name, Object args) throws Exception {
		Command command = loadCommand(name);
		if (command == null) {
			command = loadCommand(EXEC_COMMAND);
			command.setArgs(name, args);
		} else {
			command.setArgs(args);
		}
		command.setEnvironment(this);
		executeCommandAndWait(command);
		return command;
	}

	/**
	 * Returns the script variable or command if it is not a property of the
	 * environment.
	 * 
	 * @throws Exception
	 *             if some errors are encountered from the command.
	 */
	public Object propertyMissing(String name) throws Exception {
		if (variables.containsKey(name)) {
			return variables.get(name);
		} else {
			Command command = loadCommand(name);
			log.checkCommand(this, command, name);
			command.setEnvironment(this);
			switch (getExecutionMode()) {
			case IMPLICIT:
				command.args();
				executeCommandAndWait(command);
				return command;
			case EXPLICIT:
				return command;
			default:
				return command;
			}
		}
	}

	private Command loadCommand(String name) {
		CommandService service = loadCommandService(name);
		return service == null ? null : service.getCommandFactory().create();
	}

	private CommandService loadCommandService(String name) {
		log.loadCommand(this, name);
		for (CommandService service : ServiceLoader.load(CommandService.class)) {
			if (service.getInfo().getId().equals(name)) {
				return service;
			}
		}
		return null;
	}

	@Override
	public ListenableFuture<Command> executeCommand(Command command,
			PropertyChangeListener... listeners) {
		ListenableFuture<Command> task;
		task = threads.submit(command, listeners);
		return task;
	}

	@Override
	public void executeCommandAndWait(Command command) throws CommandException {
		Future<Command> task = threads.submit(command);
		try {
			task.get();
		} catch (InterruptedException e) {
			throw log.commandInterrupted(e, command);
		} catch (java.util.concurrent.ExecutionException e) {
			throw log.commandError(e.getCause(), command);
		} catch (CancellationException e) {
			throw log.commandCanceled(e, command);
		}
	}

	@Override
	public List<Future<?>> getBackgroundTasks() {
		return threads.getTasks();
	}

	@Override
	public List<Future<?>> shutdown() throws InterruptedException {
		List<Future<?>> canceledTasks = new ArrayList<Future<?>>();
		switch (backgroundCommandsPolicy) {
		case CANCEL:
			canceledTasks.addAll(cancelTasks(getBackgroundTasks()));
			break;
		case WAIT:
			canceledTasks.addAll(cancelTasks(threads
					.waitForTasks(backgroundCommandsTimeout)));
			break;
		case WAIT_NO_TIMEOUT:
			threads.waitForTasks();
			break;
		}
		threads.shutdown();
		return canceledTasks;
	}

	private List<Future<?>> cancelTasks(List<Future<?>> tasks) {
		for (Future<?> future : tasks) {
			future.cancel(true);
		}
		return tasks;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("background tasks",
				getBackgroundTasks()).toString();
	}
}
