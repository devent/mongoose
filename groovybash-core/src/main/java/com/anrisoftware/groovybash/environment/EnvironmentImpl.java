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
package com.anrisoftware.groovybash.environment;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Maps.newHashMap;
import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.groovybash.core.Buildin;
import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.anrisoftware.groovybash.core.Environment;
import com.anrisoftware.groovybash.core.ExecutorServiceHandler;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Injector;

/**
 * Loads the build-in plugins and executes them in the script context.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class EnvironmentImpl extends GroovyObjectSupport implements Environment {

	private static final String ARGS_VARIABLE = "ARGS";

	private static final String WORKING_DIRECTORY_VARIABLE = "PWD";

	private static final String HOME_VARIABLE = "HOME";

	private static final String USER_HOME_VARIABLE = "USER_HOME";

	private final EnvironmentImplLogger log;

	private final ContextProperties properties;

	private final Map<String, BuildinPlugin> buildinPlugins;

	private final CallCommandWorker callCommandWorker;

	private final ExecutorServiceHandler executorServiceHandler;

	private final ArgumentsWorker argumentsWorker;

	private final Map<String, Object> variables;

	private Injector injector;

	private Logger scriptLogger;

	@Inject
	EnvironmentImpl(EnvironmentImplLogger logger,
			@Named("environmentProperties") Properties properties,
			Set<BuildinPlugin> buildins, ArgumentsWorker argumentsWorker,
			CallCommandWorker callCommandWorker,
			ExecutorServiceHandler executorServiceHandler) {
		super();
		this.log = logger;
		this.properties = new ContextProperties(this, properties);
		this.buildinPlugins = newHashMap();
		this.callCommandWorker = callCommandWorker;
		this.argumentsWorker = argumentsWorker;
		this.executorServiceHandler = executorServiceHandler;
		this.variables = newHashMap();
		loadBuildins(buildins);
		setupVariables();
	}

	private void loadBuildins(Set<BuildinPlugin> plugins) {
		for (BuildinPlugin plugin : plugins) {
			buildinPlugins.put(plugin.getName(), plugin);
		}
	}

	private void setupVariables() {
		setWorkingDirectory(new File("."));
		setArguments(new String[0]);
		setHomeDirectory();
	}

	private void setHomeDirectory() {
		variables.put(USER_HOME_VARIABLE,
				new File(System.getProperty("user.home")));
		variables.put(HOME_VARIABLE, new File(System.getProperty("user.home")));
	}

	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	@Override
	public void setArguments(String[] args) {
		variables.put(ARGS_VARIABLE, copyOf(args));
		log.argumentsSet(args);
	}

	@Override
	public String[] getArguments() {
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
	public void setScriptLoggerContext(Class<?> context) {
		scriptLogger = LoggerFactory.getLogger(context);
	}

	@Override
	public Logger getScriptLogger() {
		return scriptLogger;
	}

	/**
	 * Call the specified command.
	 */
	@Override
	public Object invokeMethod(String name, Object args) {
		Object[] uargs = InvokerHelper.asUnwrappedArray(args);
		if (getMetaClass().getMetaMethod(name, uargs) != null) {
			return super.invokeMethod(name, args);
		}
		BuildinPlugin buildinPlugin = buildinPlugins.get(name);
		if (buildinPlugin != null) {
			return callBuildin(uargs, buildinPlugin);
		} else {
			return callCommand(name, uargs);
		}
	}

	private Object callBuildin(Object[] uargs, BuildinPlugin buildinPlugin) {
		Buildin buildin = buildinPlugin.getBuildin(injector);
		buildin.setEnvironment(this);
		return callCommandWorker.call(buildin, uargs);
	}

	private Object callCommand(String name, Object[] uargs) {
		Buildin buildin = getBuildin("run");
		ArgumentsWorker arguments;
		arguments = argumentsWorker.createCommandArgs(name, uargs);
		Object[] args = arguments.getArgs();
		Map<?, ?> flags = arguments.getFlags();
		return callCommandWorker.callWithFlags(buildin, flags, args);
	}

	private Buildin getBuildin(String name) {
		BuildinPlugin buildinPlugin = buildinPlugins.get(name);
		Buildin buildin = buildinPlugin.getBuildin(injector);
		buildin.setEnvironment(this);
		return buildin;
	}

	@Override
	public Object getProperty(String name) {
		if (getMetaClass().hasProperty(this, name) != null) {
			return super.getProperty(name);
		} else {
			Object var = variables.get(name);
			if (var != null) {
				return var;
			}
		}
		return invokeMethod(name, null);
	}

	@Override
	public Future<?> submitTask(Runnable task) {
		return executorServiceHandler.submitTask(task);
	}

	@Override
	public void close() {
		executorServiceHandler.shutdown();
	}
}
