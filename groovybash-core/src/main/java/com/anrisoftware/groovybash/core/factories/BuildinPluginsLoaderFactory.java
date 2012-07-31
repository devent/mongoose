package com.anrisoftware.groovybash.core.factories;

import java.util.List;

import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.anrisoftware.groovybash.core.plugins.PluginsLoader;

public interface BuildinPluginsLoaderFactory {

	PluginsLoader<BuildinPlugin> create(Class<BuildinPlugin> pluginClass,
			List<String> pluginNames);
}
