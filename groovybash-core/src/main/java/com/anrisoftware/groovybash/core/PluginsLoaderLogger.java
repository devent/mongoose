package com.anrisoftware.groovybash.core;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link PluginsLoader}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PluginsLoaderLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link PluginsLoader}.
	 */
	PluginsLoaderLogger() {
		super(PluginsLoader.class);
	}

	void noPluginFound(PluginsLoader<?> loader, String pluginName) {
		log.warn(
				"No plugin with the name ``{}'' found in the plugins loader {}.",
				pluginName, loader);
	}
}
