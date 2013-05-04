package com.anrisoftware.groovybash.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.BackgroundCommandsPolicy;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.environment.ExecutionMode;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Stub for the {@link Environment}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StubEnvironment implements Environment {

	@Override
	public void setEnv(Map<String, String> env) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setArgs(List<String> args) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWorkingDirectory(File directory) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getWorkingDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScriptLoggerContext(Class<?> context) {
		// TODO Auto-generated method stub

	}

	@Override
	public Logger getScriptLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getUserHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScriptHome(File dir) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getScriptHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScriptClassLoader(ClassLoader classLoader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(Locale locale) {
		// TODO Auto-generated method stub

	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackgroundCommandsPolicy(BackgroundCommandsPolicy policy) {
		// TODO Auto-generated method stub

	}

	@Override
	public BackgroundCommandsPolicy getBackgroundCommandsPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackgroundCommandsTimeout(Duration duration) {
		// TODO Auto-generated method stub

	}

	@Override
	public Duration getBackgroundCommandsTimeout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Command> executeCommand(Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeCommandAndWait(Command command) throws CommandException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Future<?>> shutdown() throws InterruptedException {
		return new ArrayList<Future<?>>();
	}

	@Override
	public void setExecutionMode(ExecutionMode mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExecutionMode getExecutionMode() {
		// TODO Auto-generated method stub
		return null;
	}
}
