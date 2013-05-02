package com.anrisoftware.mongoose.application;

import com.anrisoftware.mongoose.buildins.BuildinsModule;
import com.anrisoftware.mongoose.environment.EnvironmentModule;
import com.anrisoftware.mongoose.executor.ExecutorModule;
import com.anrisoftware.mongoose.parameter.ParameterModule;
import com.anrisoftware.mongoose.parser.ParserModule;
import com.anrisoftware.mongoose.resources.ResourcesModule;
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
