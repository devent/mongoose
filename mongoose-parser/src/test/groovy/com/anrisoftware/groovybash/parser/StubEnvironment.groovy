package com.anrisoftware.groovybash.parser

import java.util.concurrent.Future

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.slf4j.Logger

import com.anrisoftware.mongoose.api.commans.BackgroundCommandsPolicy
import com.anrisoftware.mongoose.api.commans.Command
import com.anrisoftware.mongoose.api.commans.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException

/**
 * Stub for the {@link Environment}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StubEnvironment implements Environment {

	@Override
	public void setEnv(Map<String, String> env) {
	}

	@Override
	public void setArgs(String[] args) {
	}

	@Override
	public String[] getArgs() {
		return null;
	}

	@Override
	public void setWorkingDirectory(File directory) {
	}

	@Override
	public File getWorkingDirectory() {
		return null;
	}

	@Override
	public void setScriptLoggerContext(Class<?> context) {
	}

	@Override
	public Logger getScriptLogger() {
		return null;
	}

	@Override
	public File getUserHome() {
		return null;
	}

	@Override
	public void setScriptHome(File dir) {
	}

	@Override
	public File getScriptHome() {
		return null;
	}

	@Override
	public void setScriptClassLoader(ClassLoader classLoader) {
	}

	@Override
	public void setLocale(Locale locale) {
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public void setBackgroundCommandsPolicy(BackgroundCommandsPolicy policy) {
	}

	@Override
	public BackgroundCommandsPolicy getBackgroundCommandsPolicy() {
		return null;
	}

	@Override
	public void setBackgroundCommandsTimeout(Duration duration) {
	}

	@Override
	public Duration getBackgroundCommandsTimeout() {

		return null;
	}

	@Override
	public Future<Command> executeCommand(Command command) {

		return null;
	}

	@Override
	public void executeCommandAndWait(Command command) throws CommandException {
	}

	@Override
	List<Future<?>> shutdown() throws InterruptedException {
		[]
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
