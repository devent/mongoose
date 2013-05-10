/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.buildins.sudobuildin;

import static com.google.inject.name.Names.named;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the {@code cd} build-in command factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SudoModule extends AbstractModule {

	private static final URL SUDO_PROPERTIES = SudoModule.class
			.getResource("sudo.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Command.class,
				SudoBuildin.class).build(CommandFactory.class));
		bind(Backend.class).annotatedWith(named("sudo-default-backend")).to(
				KdesuBackend.class);
	}

	@Provides
	@Named("sudo-properties")
	ContextProperties getSudoProperties() throws IOException {
		return new ContextPropertiesFactory(SudoBackend.class)
				.withDefaultProperties(System.getProperties()).fromResource(
						SUDO_PROPERTIES);
	}
}
