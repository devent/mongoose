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
package com.anrisoftware.groovybash.core.factories;

import java.util.List;

import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.anrisoftware.groovybash.core.plugins.PluginsLoader;

/**
 * Factory to create a new plug-in loader that loads the available build-in
 * plug-ins.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BuildinPluginsLoaderFactory {

	/**
	 * Creates a new plug-in loader that loads the available build-in plug-ins.
	 * 
	 * @param pluginClass
	 *            the {@link Class} of the build-in plug-in.
	 * 
	 * @param pluginNames
	 *            a {@link List} of build-in plug-in names.
	 * 
	 * @return the {@link PluginsLoader}.
	 */
	PluginsLoader<BuildinPlugin> create(Class<BuildinPlugin> pluginClass,
			List<String> pluginNames);
}
