package com.anrisoftware.groovybash.application;

import com.anrisoftware.groovybash.core.buildins.BuildinsModule;
import com.anrisoftware.groovybash.core.buildins.returns.ReturnsModule;
import com.anrisoftware.groovybash.core.parser.ParserModule;
import com.anrisoftware.groovybash.core.plugins.PluginsModule;
import com.anrisoftware.groovybash.environment.EnvironmentModule;
import com.anrisoftware.groovybash.executor.ExecutorModule;
import com.anrisoftware.groovybash.parameter.ParameterModule;
import com.google.inject.AbstractModule;

/**
 * Install all needed modules for the application.
 * 
 * @author Erwin Müller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ParameterModule());
		install(new ParserModule());
		install(new EnvironmentModule());
		install(new PluginsModule());
		install(new BuildinsModule());
		install(new ExecutorModule());
		install(new ReturnsModule());
	}
}
