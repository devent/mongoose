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

import static com.anrisoftware.mongoose.api.commans.BackgroundCommandsPolicy.POLICY_FORMAT;
import static com.anrisoftware.mongoose.resources.LocaleHooks.DISPLAY_LOCALE_PROPERTY;
import static java.util.Collections.synchronizedList;
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

import com.anrisoftware.mongoose.api.commans.BackgroundCommandsPolicy;
import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.anrisoftware.mongoose.api.commans.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.resources.LocaleHooks;
import com.anrisoftware.mongoose.resources.TemplatesResources;
import com.anrisoftware.mongoose.resources.TextsResources;
import com.anrisoftware.mongoose.threads.PropertiesThreads;
import com.anrisoftware.mongoose.threads.PropertiesThreadsFactory;
import com.anrisoftware.mongoose.threads.PropertyListenerFuture;
import com.anrisoftware.mongoose.threads.PropertyListenerFuture.Status;
import com.anrisoftware.mongoose.threads.ThreadsException;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Provides the environment variables and loads commands as Java service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EnvironmentImpl implements Environment {

	private static final String BACKGROUND_COMMANDS_TIMEOUT_PROPERTY = "background_commands_timeout";

	private static final String BACKGROUND_COMMANDS_POLICY_PROPERTY = "background_commands_policy";

	private final EnvironmentImplLogger log;

	private final PropertiesThreads threads;

	private final Map<String, Object> variables;

	private final TextsResources textsResources;

	private final TemplatesResources templatesResources;

	private final List<Future<Command>> backgroundTasks;

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
		this.textsResources = textsResources;
		this.templatesResources = templatesResources;
		this.backgroundTasks = synchronizedList(new ArrayList<Future<Command>>());
		this.backgroundCommandsPolicy = properties.getTypedProperty(
				BACKGROUND_COMMANDS_POLICY_PROPERTY, POLICY_FORMAT);
		this.backgroundCommandsTimeout = Duration.parse(properties
				.getProperty(BACKGROUND_COMMANDS_TIMEOUT_PROPERTY));
		setupLocaleHooks(localeHooks);
		setupVariables();
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

	private void setHomeDirectory() {
		variables.put(USER_HOME_VARIABLE,
				new File(System.getProperty("user.home")));
		variables.put(HOME_VARIABLE, new File(System.getProperty("user.home")));
	}

	private void setResources() {
		variables.put(TEXTS_VARIABLE, textsResources);
		variables.put(TEMPLATES_VARIABLE, templatesResources);
	}

	@Override
	public void setEnv(Map<String, String> env) {
		variables.putAll(env);
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

	/**
	 * Returns the script variable or command if it is not a property of the
	 * environment.
	 * 
	 * @throws Exception
	 */
	public Object propertyMissing(String name) throws Exception {
		if (variables.containsKey(name)) {
			return variables.get(name);
		} else {
			Command command = loadCommand(name);
			command.setEnvironment(this);
			command.setArgs(commandName(name));
			return command;
		}
	}

	private Command loadCommand(String name) {
		CommandService service = loadCommandService(name);
		if (service == null) {
			service = loadCommandService("run");
		}
		return service.getCommandFactory().create();
	}

	private CommandService loadCommandService(String name) {
		for (CommandService service : ServiceLoader.load(CommandService.class)) {
			if (service.getInfo().getId().equals(name)) {
				return service;
			}
		}
		return null;
	}

	private Map<String, Object> commandName(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commandName", name);
		return map;
	}

	@Override
	public Future<Command> executeCommand(Command command) {
		final PropertyListenerFuture<Command> task;
		task = (PropertyListenerFuture<Command>) threads.submit(command);
		task.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Status status = (Status) evt.getNewValue();
				if (status == Status.DONE) {
					backgroundTasks.remove(task);
				}
			}
		});
		backgroundTasks.add(task);
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
	public List<Future<?>> shutdown() throws InterruptedException {
		List<Future<?>> canceledTasks = new ArrayList<Future<?>>();
		switch (backgroundCommandsPolicy) {
		case CANCEL:
			canceledTasks.addAll(cancelTasks(copyBackgroundTasks()));
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

	private ArrayList<Future<?>> copyBackgroundTasks() {
		synchronized (backgroundTasks) {
			return new ArrayList<Future<?>>(backgroundTasks);
		}
	}

	private List<Future<?>> cancelTasks(List<Future<?>> tasks) {
		for (Future<?> future : tasks) {
			future.cancel(true);
		}
		return tasks;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("variables", variables)
				.toString();
	}
}
