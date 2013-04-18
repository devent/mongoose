package com.anrisoftware.groovybash.application;

import com.anrisoftware.groovybash.buildins.BuildinsModule;
import com.anrisoftware.groovybash.environment.EnvironmentModule;
import com.anrisoftware.groovybash.executor.ExecutorModule;
import com.anrisoftware.groovybash.parameter.ParameterModule;
import com.anrisoftware.groovybash.parser.ParserModule;
import com.anrisoftware.groovybash.resources.ResourcesModule;
import com.google.inject.AbstractModule;

/**
 * Install all needed modules for the application.
 * 
 * @author Erwin Müller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ParameterModule());
		install(new ParserModule());
		install(new EnvironmentModule());
		install(new BuildinsModule());
		install(new ExecutorModule());
		install(new ResourcesModule());
	}
}
