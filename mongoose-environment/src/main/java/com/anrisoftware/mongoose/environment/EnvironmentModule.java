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
package com.anrisoftware.mongoose.environment;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.propertiesutils.ContextProperties;
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

	private static final URL THREADS_PROPERTIES = EnvironmentModule.class
			.getResource("/threads.properties");

	private static final URL ENVIRONMENT_PROPERTIES = EnvironmentModule.class
			.getResource("/environment.properties");

	@Override
	protected void configure() {
		bind(Environment.class).to(EnvironmentImpl.class);
	}

	@Provides
	@Singleton
	@Named("threads-properties")
	Properties getThreadsProperties() throws IOException {
		return new ContextPropertiesFactory(
				"com.anrisoftware.globalpom.threads.properties")
				.fromResource(THREADS_PROPERTIES);
	}

	@Provides
	@Singleton
	@Named("environment-properties")
	ContextProperties getEnvironmentProperties() throws IOException {
		return new ContextPropertiesFactory(this)
				.fromResource(ENVIRONMENT_PROPERTIES);
	}
}
