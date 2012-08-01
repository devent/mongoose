package com.anrisoftware.groovybash.core.parser;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.groovybash.core.api.Environment;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

public class BashParser implements Runnable {

	private final Script script;

	private final Environment environment;

	private final ParserMetaClass parserMetaClass;

	@Inject
	BashParser(Environment environment, ParserMetaClass parserMetaClass,
			@Assisted String scriptText) {
		this.environment = environment;
		this.parserMetaClass = parserMetaClass;
		this.script = createScript(scriptText);
		setup();
	}

	private Script createScript(String scriptText) {
		ImportCustomizer imports = new ImportCustomizer()
				.addStarImports("com.anrisoftware.groovybash.core.exceptions");
		CompilerConfiguration config = new CompilerConfiguration();
		config.addCompilationCustomizers(imports);
		Script script = new GroovyShell(config).parse(scriptText);
		parserMetaClass.setDelegate(script, environment);
		return script;
	}

	private void setup() {
	}

	public void setInjector(Injector injector) {
		environment.setInjector(injector);
	}

	@Override
	public void run() {
		script.run();
	}

}
