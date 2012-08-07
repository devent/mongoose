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
package com.anrisoftware.groovybash.core.buildins.logbuildins;

import static java.lang.String.format;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.google.inject.Injector;

/**
 * Returns the debug build-in as a plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@PluginImplementation
public class DebugPlugin implements BuildinPlugin {

	@Override
	@Capabilities
	public String[] getCapabilities() {
		return new String[] { format("buildin:%s", getName()) };
	}

	@Override
	public Buildin getBuildin(Injector injector) {
		Injector childInjector = injector
				.createChildInjector(new DebugModule());
		return childInjector.getInstance(Buildin.class);
	}

	@Override
	public String getName() {
		return "debug";
	}

}
