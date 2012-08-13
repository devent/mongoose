package com.anrisoftware.groovybash.parser

import java.util.concurrent.Future

import org.slf4j.Logger

import com.anrisoftware.groovybash.core.Environment
import com.google.inject.Injector

/**
 * Stub for the {@link Environment}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
class StubEnvironment implements Environment {

	@Override
	void setInjector(Injector injector) {
	}

	@Override
	void setArguments(String[] args) {
	}

	@Override
	String[] getArguments() {
		return null
	}

	@Override
	void setWorkingDirectory(File directory) {
	}

	@Override
	File getWorkingDirectory() {
		return null
	}

	@Override
	void setScriptLoggerContext(Class<?> context) {
	}

	@Override
	Logger getScriptLogger() {
		return null
	}

	@Override
	File getUserHome() {
		return null
	}

	@Override
	Future<?> submitTask(Runnable task) {
		return null
	}

	@Override
	void close() {
	}
}
