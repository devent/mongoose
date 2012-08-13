package com.anrisoftware.groovybash.parser

import java.util.concurrent.Future

import org.slf4j.Logger

import com.anrisoftware.groovybash.core.Environment
import com.google.inject.Injector

class StubEnvironment implements Environment {

	@Override
	void setInjector(Injector injector) {
		// TODO Auto-generated method stub

	}

	@Override
	void setArguments(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	String[] getArguments() {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	void setWorkingDirectory(File directory) {
		// TODO Auto-generated method stub

	}

	@Override
	File getWorkingDirectory() {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	void setScriptLoggerContext(Class<?> context) {
		// TODO Auto-generated method stub

	}

	@Override
	Logger getScriptLogger() {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	File getUserHome() {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	Future<?> submitTask(Runnable task) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	void close() {
		// TODO Auto-generated method stub

	}
}
