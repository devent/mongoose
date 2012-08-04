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
package com.anrisoftware.groovybash.core.parser;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.groovybash.core.api.Environment;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

/**
 * Parse and run the script. Sets the environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
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

	public void setInjector(Injector injector) {
		environment.setInjector(injector);
	}

	@Override
	public void run() {
		script.run();
		environment.close();
	}

}
