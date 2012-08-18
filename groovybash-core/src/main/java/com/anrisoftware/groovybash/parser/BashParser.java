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
package com.anrisoftware.groovybash.parser;

import static org.apache.commons.lang3.StringUtils.replace;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.groovybash.core.Environment;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

/**
 * Parse and run the script. Sets the environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class BashParser implements Runnable {

	private static final String ADD_PACKAGE_AS_CLASSPATH_PROPERTY = "add_package_as_classpath";

	private static final String IMPORTS_PROPERTY = "imports";

	private static final String STAR_IMPORTS_PROPERTY = "star_imports";

	private final BashParserLogger log;

	private final Script script;

	private final Environment environment;

	private final ParserMetaClass parserMetaClass;

	private final ContextProperties parserProperties;

	private final String scriptHome;

	/**
	 * Sets the dependencies of the parser.
	 * 
	 * @param properties
	 *            the parser {@link Properties}. Expects the properties:
	 *            <dl>
	 *            <dt>{@code com.anrisoftware.groovybash.parser.star_imports}</dt>
	 *            <dd>
	 *            A list of all packages that should be imported in the script
	 *            as a star import. Star import will import all classes in the
	 *            package.</dd>
	 * 
	 *            <dt>{@code com.anrisoftware.groovybash.parser.imports}</dt>
	 *            <dd>A list of all classes that should be imported in the
	 *            script.</dd>
	 * 
	 *            <dt>{@code com.anrisoftware.groovybash.parser.script_home}</dt>
	 *            <dd>The home directory of the script. Defaults to the user's
	 *            current working directory.</dd>
	 *            </dl>
	 * 
	 * @param environment
	 *            the {@link Environment} of the script.
	 * 
	 * @param parserMetaClass
	 *            the {@link ParserMetaClass} that sets the environment as the
	 *            delegate.
	 * 
	 * @param scriptText
	 *            the script text to run.
	 */
	@Inject
	BashParser(BashParserLogger logger,
			@Named("parser-properties") Properties properties,
			Environment environment, ParserMetaClass parserMetaClass,
			@Assisted String scriptText) {
		this.log = logger;
		this.parserProperties = new ContextProperties(this, properties);
		this.environment = environment;
		this.parserMetaClass = parserMetaClass;
		this.scriptHome = properties.getProperty("script_home",
				System.getProperty("user.dir"));
		this.script = createScript(scriptText);
	}

	private Script createScript(String scriptText) {
		CompilerConfiguration config = new CompilerConfiguration();
		CompilationCustomizer imports = createImports();
		config.addCompilationCustomizers(imports);
		Script script = new GroovyShell(config).parse(scriptText);
		addPackageNameToClassPath(script);

		parserMetaClass.setDelegate(script, environment);
		environment.setScriptClassLoader(script.getClass().getClassLoader());
		environment.setScriptLoggerContext(script.getClass());
		return script;
	}

	private void addPackageNameToClassPath(Script script) {
		if (!parserProperties.getBooleanProperty(
				ADD_PACKAGE_AS_CLASSPATH_PROPERTY, false)) {
			return;
		}

		String path;
		GroovyClassLoader loader;
		loader = (GroovyClassLoader) script.getClass().getClassLoader();
		Package scriptPackage = script.getClass().getPackage();
		if (scriptPackage == null) {
			path = scriptHome;
		} else {
			String name = replace(scriptPackage.getName(), ".", "/");
			path = new File(scriptHome, name).getAbsolutePath();
		}
		loader.addClasspath(path);
		log.addClasspathToScript(path);
	}

	private ImportCustomizer createImports() {
		ImportCustomizer imports = new ImportCustomizer();
		addStarImports(imports);
		addImports(imports);
		return imports;
	}

	private void addStarImports(ImportCustomizer imports) {
		List<String> list = parserProperties
				.getListProperty(STAR_IMPORTS_PROPERTY);
		imports.addStarImports(list.toArray(new String[list.size()]));
	}

	private void addImports(ImportCustomizer imports) {
		List<String> list = parserProperties.getListProperty(IMPORTS_PROPERTY);
		imports.addImports(list.toArray(new String[list.size()]));
	}

	/**
	 * Sets the parent injector for the script.
	 * 
	 * @param injector
	 *            the {@link Inject}
	 */
	public void setInjector(Injector injector) {
		environment.setInjector(injector);
	}

	/**
	 * Sets the command line arguments for the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	public void setArguments(String[] args) {
		environment.setArguments(args);
	}

	/**
	 * Runs the script.
	 */
	@Override
	public void run() {
		script.run();
		environment.close();
	}

}
