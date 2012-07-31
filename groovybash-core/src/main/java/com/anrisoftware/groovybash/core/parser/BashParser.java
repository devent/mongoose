package com.anrisoftware.groovybash.core.parser;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.api.Environment;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

public class BashParser implements Runnable {

	private final Script script;

	private final Environment environment;

	private final ParserMetaClass parserMetaClass;

	@Inject
	BashParser(Environment environment, ParserMetaClass parserMetaClass,
			@Assisted String script) {
		this.environment = environment;
		this.script = new GroovyShell().parse(script);
		this.parserMetaClass = parserMetaClass;
		setup();
	}

	private void setup() {
		parserMetaClass.setDelegate(script, environment);
	}

	public void setInjector(Injector injector) {
		environment.setInjector(injector);
	}

	@Override
	public void run() {
		script.run();
	}

}
