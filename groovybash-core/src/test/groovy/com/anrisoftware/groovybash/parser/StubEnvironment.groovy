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
	}

	@Override
	void setWorkingDirectory(File directory) {
	}

	@Override
	File getWorkingDirectory() {
	}

	@Override
	void setScriptLoggerContext(Class<?> context) {
	}

	@Override
	Logger getScriptLogger() {
	}

	@Override
	File getUserHome() {
	}

	@Override
	void setScriptHome(File dir) {
	}

	@Override
	File getScriptHome() {
	}

	@Override
	void setScriptClassLoader(ClassLoader classLoader) {
	}

	@Override
	Future<?> submitTask(Runnable task) {
	}

	@Override
	void close() {
	}
}
