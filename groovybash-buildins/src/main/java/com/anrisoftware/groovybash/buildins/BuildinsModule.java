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
package com.anrisoftware.groovybash.buildins;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.anrisoftware.groovybash.buildins.cdbuildin.CdPlugin;
import com.anrisoftware.groovybash.buildins.echobuildin.EchoPlugin;
import com.anrisoftware.groovybash.buildins.listfilesbuildin.ListFilesPluginModule;
import com.anrisoftware.groovybash.buildins.runbuildin.RunPluginModule;
import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

/**
 * Install the build-in modules. Provides a default of the the standard input
 * and output streams for build-ins commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class BuildinsModule extends AbstractModule {

	@Override
	protected void configure() {
		installBuildins();
	}

	private void installBuildins() {
		Multibinder<BuildinPlugin> binder;
		binder = newSetBinder(binder(), BuildinPlugin.class);
		binder.addBinding().to(CdPlugin.class);
		binder.addBinding().to(EchoPlugin.class);

		install(new RunPluginModule());
		install(new ListFilesPluginModule());
	}

	/**
	 * Provides a default of the the standard input and output streams for
	 * build-ins commands.
	 * 
	 * @return the {@link StandardStreams} that returns the standard input and
	 *         output streams.
	 */
	@Provides
	protected StandardStreams getStandardStreams() {
		return new StandardStreams(System.in, System.out, System.err);
	}
}
