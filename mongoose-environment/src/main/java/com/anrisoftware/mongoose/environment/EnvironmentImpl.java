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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.anrisoftware.mongoose.api.commans.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.resources.TemplatesResources;
import com.anrisoftware.mongoose.resources.TextsResources;
import com.anrisoftware.mongoose.threads.PropertiesThreads;
import com.anrisoftware.mongoose.threads.PropertiesThreadsFactory;

/**
 * Provides the environment variables and loads commands as Java service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EnvironmentImpl implements Environment {

	private final EnvironmentImplLogger log;

	private final PropertiesThreads threads;

	private final Map<String, Object> variables;

	private final TextsResources textsResources;

	private final TemplatesResources templatesResources;

	private Logger scriptLogger;

	@Inject
	EnvironmentImpl(EnvironmentImplLogger logger,
			@Named("threads-properties") Properties properties,
			PropertiesThreadsFactory threadsFactory,
			TextsResources textsResources, TemplatesResources templatesResources) {
		this.log = logger;
		this.threads = threadsFactory.create(properties, "script");
		this.variables = new HashMap<String, Object>();
		this.textsResources = textsResources;
		this.templatesResources = templatesResources;
		setupVariables();
	}

	private void setupVariables() {
		setEnv(System.getenv());
		setArgs(new String[0]);
		setScriptHome(new File("."));
		setWorkingDirectory(new File("."));
		setHomeDirectory();
		setResources();
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
	public void setArgs(String[] args) {
		variables.put(ARGS_VARIABLE, ArrayUtils.clone(args));
		log.argumentsSet(args);
	}

	@Override
	public String[] getArgs() {
		List<String> args = asList(String.class);
		return args.toArray(new String[args.size()]);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> asList(Class<T> type) {
		return (List<T>) variables.get(ARGS_VARIABLE);
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
		return threads.submit(command);
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
	public void shutdown() {
		threads.shutdown();
	}
}
