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
package com.anrisoftware.mongoose.buildins.runbuildin;

import static org.apache.commons.lang3.StringUtils.split;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ProcessDestroyer;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.joda.time.Duration;

import com.anrisoftware.mongoose.api.commans.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * Executes the specified command in a separate process with the specified
 * environment and working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RunBuildin extends AbstractCommand {

	private static final String TIMEOUT_KEY = "timeout";

	private static final String WATCHDOG_KEY = "watchdog";

	private static final String DESTROYER_KEY = "destroyer";

	private static final String HANDLER_KEY = "handler";

	private final RunBuildinLogger log;

	private final Executor executor;

	private CommandLine command;

	private Map<String, String> env;

	private File workingDirectory;

	private ExecuteResultHandler handler;

	private ProcessDestroyer destroyer;

	private ExecuteWatchdog watchdog;

	@Inject
	RunBuildin(RunBuildinLogger logger, Executor executor) {
		this.log = logger;
		this.executor = executor;
		this.env = null;
		this.handler = null;
		this.destroyer = null;
		this.watchdog = null;
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		setDirectory(environment.getWorkingDirectory());
	}

	/**
	 * Returns the name {@code run}.
	 */
	@Override
	public String getTheName() {
		return "run";
	}

	@Override
	public RunBuildin call() throws Exception {
		startProcess();
		return this;
	}

	private void startProcess() throws IOException {
		executor.setProcessDestroyer(destroyer);
		executor.setWatchdog(watchdog);
		executor.setWorkingDirectory(workingDirectory);
		executor.setStreamHandler(getStreams().getStreamHandler());
		executor.execute(command, env, handler);
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		super.argumentsSet(args, unnamedArgs);
		if (args.containsKey(HANDLER_KEY)) {
			setHandler(args.get(HANDLER_KEY));
		}
		if (args.containsKey(DESTROYER_KEY)) {
			setDestroyer(args.get(DESTROYER_KEY));
		}
		if (args.containsKey(WATCHDOG_KEY)) {
			setWatchdog(args.get(WATCHDOG_KEY));
		}
		if (args.containsKey(TIMEOUT_KEY)) {
			setTimeout(args.get(TIMEOUT_KEY));
		}
		unnamedArgsSet(unnamedArgs);
	}

	public void setTimeout(Object object) throws CommandException {
		if (object instanceof Duration) {
			Duration timeout = (Duration) object;
			setWatchdog(new ExecuteWatchdog(timeout.getMillis()));
		} else if (object instanceof Number) {
			Number timeout = (Number) object;
			setWatchdog(new ExecuteWatchdog(timeout.longValue()));
		} else {
			Duration timeout = Duration.parse(object.toString());
			setWatchdog(new ExecuteWatchdog(timeout.getMillis()));
		}
	}

	public void setHandler(Object object) throws CommandException {
		if (object instanceof Class) {
			@SuppressWarnings("unchecked")
			Class<ExecuteResultHandler> type = (Class<ExecuteResultHandler>) object;
			this.handler = createType(type);
		} else if (object instanceof ExecuteResultHandler) {
			this.handler = (ExecuteResultHandler) object;
		} else {
			throw log.errorHandlerType(this, object);
		}
		log.handlerSet(this, object);
	}

	public void setDestroyer(Object object) throws CommandException {
		if (object instanceof Class) {
			@SuppressWarnings("unchecked")
			Class<ProcessDestroyer> type = (Class<ProcessDestroyer>) object;
			this.destroyer = createType(type);
		} else if (object instanceof ExecuteResultHandler) {
			this.destroyer = (ProcessDestroyer) object;
		} else {
			throw log.errorDestroyerType(this, object);
		}
		log.destroyerSet(this, object);
	}

	public void setWatchdog(Object object) throws CommandException {
		if (object instanceof Class) {
			@SuppressWarnings("unchecked")
			Class<ExecuteWatchdog> type = (Class<ExecuteWatchdog>) object;
			this.watchdog = createType(type);
		} else if (object instanceof ExecuteResultHandler) {
			this.watchdog = (ExecuteWatchdog) object;
		} else {
			throw log.errorWatchdogType(this, object);
		}
		log.watchdogSet(this, object);
	}

	private <T> T createType(Class<T> type) throws CommandException {
		try {
			return ConstructorUtils.invokeConstructor(type);
		} catch (NoSuchMethodException e) {
			throw log.noDefaultCtor(this, e, type);
		} catch (IllegalAccessException e) {
			throw log.noDefaultCtor(this, e, type);
		} catch (InvocationTargetException e) {
			throw log.errorInstantiate(this, e, type);
		} catch (InstantiationException e) {
			throw log.errorInstantiate(this, e, type);
		}
	}

	private void unnamedArgsSet(List<Object> args) {
		setCommand(args.get(0));
		if (args.size() > 1) {
			setEnv(args.get(1));
		}
		if (args.size() > 2) {
			setDirectory(args.get(2));
		}
	}

	public void setCommand(Object object) {
		this.command = CommandLine.parse(object.toString());
		log.commandSet(this, this.command);
	}

	public void setEnv(Object object) {
		if (object instanceof Map) {
			this.env = asMap(object);
		} else {
			this.env = parseEnvironment(object.toString());
		}
		log.envSet(this, this.env);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> asMap(Object obj) {
		return (Map<String, String>) obj;
	}

	private Map<String, String> parseEnvironment(String string) {
		String[] strings = split(string, " ;,");
		Map<String, String> map = new HashMap<String, String>(
				strings.length / 2);
		for (String tuple : strings) {
			String[] entry = split(tuple, "=");
			map.put(entry[0], entry[1]);
		}
		return map;
	}

	public void setDirectory(Object object) {
		executor.setWorkingDirectory(asFile(object));
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		} else {
			return new File(object.toString());
		}
	}

}
