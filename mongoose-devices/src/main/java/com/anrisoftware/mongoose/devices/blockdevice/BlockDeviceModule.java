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
package com.anrisoftware.mongoose.devices.blockdevice;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.Class.forName;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.CommandFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * Installs the block device factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BlockDeviceModule extends AbstractModule {

	private static final URL BLOCK_DEVICE_PROPERTIES = BlockDeviceModule.class
			.getResource("/blockdevice.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Command.class,
				BlockDevice.class).build(CommandFactory.class));
		bindResizeTasks();
	}

	@Singleton
	@Provides
	@Named("blockdevice.properties")
	ContextProperties getBlockDeviceProperties() throws IOException {
		return new ContextPropertiesFactory(this)
				.fromResource(BLOCK_DEVICE_PROPERTIES);
	}

	private void bindResizeTasks() {
		try {
			bindResizeTasks0();
		} catch (IOException e) {
			addError(e);
		} catch (ClassNotFoundException e) {
			addError(e);
		}
	}

	private void bindResizeTasks0() throws IOException, ClassNotFoundException {
		MapBinder<String, ResizeTask> mapbinder = newMapBinder(binder(),
				String.class, ResizeTask.class);
		ContextProperties p = getBlockDeviceProperties();
		List<String> list = p.getListProperty("resize_tasks");
		for (String string : list) {
			String[] split = split(string, ':');
			@SuppressWarnings("unchecked")
			Class<ResizeTask> name = (Class<ResizeTask>) forName(split[1]);
			mapbinder.addBinding(split[0]).to(name);
		}
	}
}
