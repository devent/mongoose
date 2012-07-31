package com.anrisoftware.groovybash.core.environment;

import static com.google.common.io.Resources.getResource;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;

import com.anrisoftware.groovybash.core.api.Environment;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Provides the environment properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EnvironmentModule extends AbstractModule {

	private static final URL ENVIRONMENT_PROPERTIES = getResource(
			EnvironmentModule.class, "environment.properties");

	@Override
	protected void configure() {
		bind(Environment.class).to(EnvironmentImpl.class);
	}

	@Provides
	@Named("environmentProperties")
	Properties getEnvironmentProperties() throws IOException {
		return new ContextPropertiesFactory(this)
				.fromResource(ENVIRONMENT_PROPERTIES);
	}
}
