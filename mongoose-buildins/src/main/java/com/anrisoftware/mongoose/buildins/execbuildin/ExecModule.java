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
package com.anrisoftware.mongoose.buildins.execbuildin;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the {@code exec} build-in command factory.
 * 
 * @see Command
 * @see CommandFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecModule extends AbstractModule {

	private static final URL EXEC_PROPERTIES = ExecModule.class
			.getResource("/exec.properties");

	@Override
	protected void configure() {
		bind(Command.class).to(ExecBuildin.class);
		install(new FactoryModuleBuilder().implement(Command.class,
				ExecBuildin.class).build(CommandFactory.class));
		bind(Executor.class).to(DefaultExecutor.class);
	}

	@Provides
	@Named("exec-properties")
	ContextProperties getExecProperties() throws IOException {
		return new ContextPropertiesFactory(ExecBuildin.class)
				.withDefaultProperties(System.getProperties()).fromResource(
						EXEC_PROPERTIES);
	}

}
