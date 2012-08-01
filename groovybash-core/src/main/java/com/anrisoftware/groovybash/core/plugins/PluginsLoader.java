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
package com.anrisoftware.groovybash.core.plugins;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads and returns all available plug-ins with a specified class.
 * 
 * @param PluginType
 *            the type of the plug-in. Needs to implement the {@link Plugin}
 *            interface.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PluginsLoader<PluginType extends Plugin> implements
		Callable<List<PluginType>> {

	private final PluginsLoaderLogger log;

	private final PluginManager manager;

	private final Class<PluginType> pluginClass;

	private final List<String> names;

	/**
	 * Sets the specified plug-in class and plug-ins properties.
	 * 
	 * @param manager
	 *            the {@link PluginManager}.
	 * 
	 * @param pluginClass
	 *            the plug-in {@link Class}, needs to implement the interface
	 *            {@link Plugin}.
	 * 
	 * @param pluginNames
	 *            the names of the plug-ins to load.
	 * 
	 */
	@Inject
	PluginsLoader(PluginsLoaderLogger logger, PluginManager manager,
			@Assisted Class<PluginType> pluginClass,
			@Assisted List<String> pluginNames) {
		this.log = logger;
		this.manager = manager;
		this.pluginClass = pluginClass;
		this.names = pluginNames;
	}

	/**
	 * Loads the plug-ins and returns them as a list.
	 * 
	 * @return an immutable {@link List} of the loaded plug-ins.
	 */
	@Override
	public List<PluginType> call() throws Exception {
		List<PluginType> plugins = loadPlugins();
		return ImmutableList.copyOf(plugins);
	}

	private List<PluginType> loadPlugins() {
		List<PluginType> plugins = Lists.newArrayList();
		for (String pluginName : names) {
			PluginType plugin = loadPlugin(pluginName);
			if (plugin == null) {
				log.noPluginFound(this, pluginName);
				continue;
			}
			plugins.add(plugin);
		}
		return plugins;
	}

	private PluginType loadPlugin(String name) {
		return manager.getPlugin(pluginClass, new OptionCapabilities(name));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("plugin class", pluginClass)
				.toString();
	}
}
