/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-lodevices.
 * 
 * groovybash-lodevices is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-lodevices is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-lodevices. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.devices.lodevicebuildin;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.mongoose.api.commans.CommandFactory;
import com.anrisoftware.mongoose.api.commans.CommandInfo;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Makes the build-in command {@code lodevice} available as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(CommandService.class)
public class LodeviceService implements CommandService {

	/**
	 * The unique identifier of the build-in command {@code lodevice}.
	 */
	public static final String ID = "lodevice";

	/**
	 * The command information of the build-in command {@code lodevice}.
	 */
	public static final CommandInfo INFO = new CommandInfo() {

		@Override
		public String getId() {
			return ID;
		}
	};

	private static final Module[] MODULES = new Module[] { new LodeviceModule() };

	@Override
	public CommandInfo getInfo() {
		return INFO;
	}

	@Override
	public CommandFactory getCommandFactory(Object... external) {
		return createInjector(
				(Injector) (external.length > 0 ? external[0] : null))
				.getInstance(CommandFactory.class);
	}

	private Injector createInjector(Injector parent) {
		return parent == null ? Guice.createInjector(MODULES) : parent
				.createChildInjector(MODULES);
	}
}
