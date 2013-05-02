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
package com.anrisoftware.mongoose.parser;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.mongoose.api.commans.Environment;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Parse and run the script. Sets the environment of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ScriptParser implements Callable<ScriptParser> {

	private static final String USER_DIR_PROPERTY = "user.dir";

	private static final String SCRIPT_HOME_PROPERTY = "script_home";

	private final ScriptParserLogger log;

	private final Script script;

	private final Environment environment;

	private final ParserMetaClass parserMetaClass;

	private final String scriptHome;

	private final ImportCustomizer importCustomizer;

	private final ScriptPackageToClasspath scriptPackageToClasspath;

	private List<Future<?>> canceledCommands;

	/**
	 * @param p
	 *            the parser {@link ContextProperties} properties:
	 *            <p>
	 *            <dl>
	 * 
	 *            <dt>{@code com.anrisoftware.mongoose.parser.script_home}</dt>
	 *            <dd>The home directory of the script. Defaults to the user's
	 *            current working directory.</dd>
	 * 
	 *            </dl>
	 * 
	 * @param environment
	 *            the {@link Environment} of the script.
	 */
	@Inject
	ScriptParser(ScriptParserLogger logger,
			ImportCustomizerProvider importCustomizerProvider,
			ScriptPackageToClasspath scriptPackageToClasspath,
			ParserMetaClass parserMetaClass,
			@Named("parser-properties") ContextProperties p,
			Environment environment, @Assisted String scriptText) {
		this.log = logger;
		this.importCustomizer = importCustomizerProvider.get();
		this.scriptPackageToClasspath = scriptPackageToClasspath;
		this.environment = environment;
		this.parserMetaClass = parserMetaClass;
		this.scriptHome = p.getProperty(SCRIPT_HOME_PROPERTY,
				System.getProperty(USER_DIR_PROPERTY));
		this.script = createScript(scriptText);
	}

	private Script createScript(String scriptText) {
		CompilerConfiguration config = new CompilerConfiguration();
		config.addCompilationCustomizers(importCustomizer);
		Script script = new GroovyShell(config).parse(scriptText);
		scriptPackageToClasspath.addPackageNameToClassPath(script, scriptHome);
		parserMetaClass.setDelegate(script, environment);
		environment.setScriptClassLoader(script.getClass().getClassLoader());
		environment.setScriptLoggerContext(script.getClass());
		environment.setScriptHome(new File(scriptHome));
		return script;
	}

	/**
	 * Sets the command line arguments for the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	public void setArgs(String[] args) {
		environment.setArgs(asList(args));
	}

	/**
	 * Returns the script environment.
	 * 
	 * @return the {@link Environment}.
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * Returns a list of all canceled commands after the script was finished.
	 * 
	 * @return an unmodifiable {@link List} of the commands.
	 */
	public List<Future<?>> getCanceledCommands() {
		return canceledCommands;
	}

	/**
	 * Runs the script.
	 */
	@Override
	public ScriptParser call() throws Exception {
		try {
			log.startScript(this);
			script.run();
		} finally {
			canceledCommands = unmodifiableList(environment.shutdown());
			log.shutdownScript(this);
		}
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("environment", environment)
				.toString();
	}
}
