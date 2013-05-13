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
package com.anrisoftware.mongoose.devices.mount;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the mount factory.
 * 
 * @see Mount
 * @see MountFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MountModule extends AbstractModule {

	private static final URL MOUNT_PROPERTIES = MountModule.class
			.getResource("/mount.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Mount.class, Mount.class)
				.build(MountFactory.class));
		install(new FactoryModuleBuilder().implement(FsckTask.class,
				FsckTask.class).build(FsckTaskFactory.class));
		install(new FactoryModuleBuilder().implement(MountTask.class,
				MountTask.class).build(MountTaskFactory.class));
	}

	@Provides
	@Named("mount-properties")
	@Singleton
	ContextProperties getMountProperties() throws IOException {
		return new ContextPropertiesFactory(Mount.class).withDefaultProperties(
				System.getProperties()).fromResource(MOUNT_PROPERTIES);
	}
}
