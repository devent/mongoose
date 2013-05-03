package com.anrisoftware.mongoose.app;

import com.anrisoftware.mongoose.environment.EnvironmentModule;
import com.anrisoftware.mongoose.parser.ParserModule;
import com.anrisoftware.mongoose.resources.ResourcesModule;
import com.anrisoftware.mongoose.threads.ThreadsModule;
import com.google.inject.AbstractModule;

/**
 * Install needed modules for the application.
 * 
 * @author Erwin Müller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ParserModule());
		install(new EnvironmentModule());
		install(new ResourcesModule());
		install(new ThreadsModule());
	}
}
