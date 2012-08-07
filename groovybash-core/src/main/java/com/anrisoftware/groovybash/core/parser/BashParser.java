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

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.groovybash.core.api.Environment;
import com.anrisoftware.propertiesutils.ContextProperties;
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

	private final ContextProperties parserProperties;

	@Inject
	BashParser(@Named("parser-properties") Properties parserProperties,
			Environment environment, ParserMetaClass parserMetaClass,
			@Assisted String scriptText) {
		this.parserProperties = new ContextProperties(this, parserProperties);
		this.environment = environment;
		this.parserMetaClass = parserMetaClass;
		this.script = createScript(scriptText);
	}

	private Script createScript(String scriptText) {
		ImportCustomizer imports = new ImportCustomizer();
		addStarImports(imports);
		addImports(imports);
		CompilerConfiguration config = new CompilerConfiguration();
		config.addCompilationCustomizers(imports);
		Script script = new GroovyShell(config).parse(scriptText);
		parserMetaClass.setDelegate(script, environment);
		environment.setScriptLoggerContext(script.getClass());
		return script;
	}

	private void addStarImports(ImportCustomizer imports) {
		List<String> list = parserProperties.getListProperty("star_imports");
		imports.addStarImports(list.toArray(new String[list.size()]));
	}

	private void addImports(ImportCustomizer imports) {
		List<String> list = parserProperties.getListProperty("imports");
		imports.addImports(list.toArray(new String[list.size()]));
	}

	public void setInjector(Injector injector) {
		environment.setInjector(injector);
	}

	public void setArguments(String[] args) {
		environment.setArguments(args);
	}

	@Override
	public void run() {
		script.run();
		environment.close();
	}

}
