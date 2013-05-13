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
package com.anrisoftware.mongoose.buildins.execbuildin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ProcessDestroyer;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.joda.time.Duration;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Executes the specified command in a separate process with the specified
 * environment and working directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecBuildin extends AbstractCommand {

	private static final String TERMINAL_COMMAND_PROPERTY = "terminal_command";

	private static final String SUCCESS_EXIT_VALUES_KEY = "successExitValues";

	private static final String SUCCESS_EXIT_VALUE_KEY = "successExitValue";

	private static final String TIMEOUT_KEY = "timeout";

	private static final String WATCHDOG_KEY = "watchdog";

	private static final String DESTROYER_KEY = "destroyer";

	private static final String HANDLER_KEY = "handler";

	private static final String TERMINAL_KEY = "terminal";

	private static final String TERMINAL_COMMAND_KEY = "terminalCommand";

	private static final String TERMINAL_COMMAND_VARIABLE = "TERMINAL_COMMAND";

	private final ExecBuildinLogger log;

	private final Executor executor;

	private String command;

	private Map<String, String> env;

	private ExecuteResultHandler handler;

	private ProcessDestroyer destroyer;

	private ExecuteWatchdog watchdog;

	private String terminalCommand;

	private boolean useTerminal;

	@Inject
	ExecBuildin(ExecBuildinLogger logger, Executor executor,
			@Named("exec-properties") ContextProperties p) {
		this.log = logger;
		this.executor = executor;
		this.command = null;
		this.env = null;
		this.handler = new DefaultExecuteResultHandler();
		this.destroyer = new ShutdownHookProcessDestroyer();
		this.watchdog = null;
		this.useTerminal = false;
		this.terminalCommand = p.getProperty(TERMINAL_COMMAND_PROPERTY);
	}

	@Override
	public void setEnvironment(Environment environment) throws CommandException {
		super.setEnvironment(environment);
		setDirectory(environment.getWorkingDirectory());
		env = environment.getEnv();
		if (env.containsKey(TERMINAL_COMMAND_VARIABLE)) {
			setTerminalCommand(env.get(TERMINAL_COMMAND_VARIABLE));
		}
	}

	/**
	 * Returns the name {@code exec} or the command's executable.
	 */
	@Override
	public String getTheName() {
		return command == null ? "exec" : command;
	}

	@Override
	protected void doCall() throws Exception {
		startProcess();
	}

	private void startProcess() throws IOException, InterruptedException {
		executor.setProcessDestroyer(destroyer);
		executor.setWatchdog(watchdog);
		executor.setStreamHandler(new PumpStreamHandler(getOutput(),
				getError(), getInput()));
		executor.execute(createCommand(), env, handler);
		waitForCommand();
	}

	private CommandLine createCommand() {
		String cmd = command;
		if (useTerminal) {
			cmd = terminalCommand.replace("{}", cmd);
		}
		return CommandLine.parse(cmd);
	}

	private void waitForCommand() throws InterruptedException, CommandException {
		if (handler instanceof DefaultExecuteResultHandler) {
			DefaultExecuteResultHandler h = (DefaultExecuteResultHandler) handler;
			h.waitFor();
			ExecuteException ex = h.getException();
			if (ex != null) {
				throw log.errorCommand(this, ex);
			}
		}
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
		if (args.containsKey(SUCCESS_EXIT_VALUE_KEY)) {
			setSuccessExitValue(((Number) args.get(SUCCESS_EXIT_VALUE_KEY))
					.intValue());
		}
		if (args.containsKey(SUCCESS_EXIT_VALUES_KEY)) {
			setSuccessExitValues((List<?>) args.get(SUCCESS_EXIT_VALUES_KEY));
		}
		if (args.containsKey(TERMINAL_KEY)) {
			setTerminal((Boolean) args.get(TERMINAL_KEY));
		}
		if (args.containsKey(TERMINAL_COMMAND_KEY)) {
			setTerminalCommand(args.get(TERMINAL_COMMAND_KEY).toString());
		}
		unnamedArgsSet(unnamedArgs);
	}

	/**
	 * Sets the timeout for the process.
	 * 
	 * @param object
	 *            the {@link Duration} of the timeout; or the {@link Number}
	 *            timeout in milliseconds; or an {@link Object} that is parsed
	 *            as the duration.
	 * 
	 * @throws CommandException
	 *             if any errors set the timeout watchdog.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parsed duration is not valid.
	 */
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

	/**
	 * Sets the callback that is called when the process completes.
	 * 
	 * @param object
	 *            either the {@link ExecuteResultHandler} or a {@link Class};
	 *            the class type is instantiated with the default constructor
	 *            and set as the handler.
	 * 
	 * @throws CommandException
	 *             if the specified object is not an
	 *             {@link ExecuteResultHandler}; if there were errors
	 *             instantiate the class type.
	 */
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

	/**
	 * Destroys the process under certain conditions.
	 * 
	 * @param object
	 *            either the {@link ProcessDestroyer} or a {@link Class}; the
	 *            class type is instantiated with the default constructor and
	 *            set as the handler.
	 * 
	 * @throws CommandException
	 *             if the specified object is not an {@link ProcessDestroyer};
	 *             if there were errors instantiate the class type.
	 */
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

	/**
	 * Destroys the process after a specified timeout.
	 * 
	 * @param object
	 *            either the {@link ExecuteWatchdog} or a {@link Class}; the
	 *            class type is instantiated with the default constructor and
	 *            set as the handler.
	 * 
	 * @throws CommandException
	 *             if the specified object is not an {@link ExecuteWatchdog}; if
	 *             there were errors instantiate the class type.
	 */
	public void setWatchdog(Object object) throws CommandException {
		if (object instanceof Class) {
			@SuppressWarnings("unchecked")
			Class<ExecuteWatchdog> type = (Class<ExecuteWatchdog>) object;
			this.watchdog = createType(type);
		} else if (object instanceof ExecuteWatchdog) {
			this.watchdog = (ExecuteWatchdog) object;
		} else {
			throw log.errorWatchdogType(this, object);
		}
		log.watchdogSet(this, object);
	}

	private void unnamedArgsSet(List<Object> args) {
		setCommand(args.get(0).toString());
		if (args.size() > 1) {
			setEnv(asMap(args.get(1)));
		}
		if (args.size() > 2) {
			setDirectory(args.get(2));
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> asMap(Object obj) {
		return (Map<String, String>) obj;
	}

	/**
	 * Sets the command string.
	 * 
	 * @param object
	 *            the command string.
	 * 
	 * @throws NullPointerException
	 *             if the command string is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the command string is empty.
	 */
	public void setCommand(String command) {
		this.command = command;
		log.commandSet(this, this.command);
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
		log.envSet(this, this.env);
	}

	public void setDirectory(Object object) {
		executor.setWorkingDirectory(asFile(object));
		log.directorySet(this, object);
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		} else {
			return new File(object.toString());
		}
	}

	/**
	 * @see Executor#setExitValue(int)
	 */
	public void setSuccessExitValue(int value) {
		executor.setExitValue(value);
		log.exitValueSet(this, value);
	}

	/**
	 * @see Executor#setExitValues(int[])
	 */
	public void setSuccessExitValues(List<?> list) {
		int[] values = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object item = list.get(i);
			values[i] = ((Number) item).intValue();
		}
		executor.setExitValues(values);
		log.exitValuesSet(this, values);
	}

	/**
	 * @see DefaultExecuteResultHandler#getExitValue()
	 * 
	 * @throws IllegalStateException
	 *             if the handler is not of type
	 *             {@link DefaultExecuteResultHandler}.
	 */
	public int getTheExitValue() {
		if (handler instanceof DefaultExecuteResultHandler) {
			DefaultExecuteResultHandler h = (DefaultExecuteResultHandler) handler;
			return h.getExitValue();
		} else {
			throw log.notExitValueAvailable(this);
		}
	}

	public void setTerminalCommand(String command) {
		this.terminalCommand = command;
	}

	public void setTerminal(boolean terminal) {
		this.useTerminal = terminal;
	}

}
