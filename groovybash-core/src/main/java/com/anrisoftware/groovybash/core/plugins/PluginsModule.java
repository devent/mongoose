package com.anrisoftware.groovybash.core.plugins;

import static com.google.common.io.Resources.getResource;
import static net.xeoh.plugins.base.impl.PluginManagerFactory.createPluginManager;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.util.JSPFProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.groovybash.core.factories.BuildinPluginsLoaderFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Provides the environment properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PluginsModule extends AbstractModule {

	private static final URL PLUGINS_PROPERTIES = getResource(
			PluginsModule.class, "plugins.properties");

	private static Logger log = LoggerFactory.getLogger(PluginsModule.class);

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(
				new TypeLiteral<PluginsLoader<? extends Plugin>>() {
				}, new TypeLiteral<PluginsLoader<? extends Plugin>>() {
				}).build(BuildinPluginsLoaderFactory.class));
	}

	@Provides
	PluginManager getPluginManager() throws URISyntaxException, IOException {
		JSPFProperties props = new JSPFProperties();
		ContextProperties pluginsProperties = getPluginsProperties();
		props.setProperty(PluginManager.class, "cache.enabled",
				pluginsProperties.getProperty("cache_enabled"));
		props.setProperty(PluginManager.class, "cache.mode",
				pluginsProperties.getProperty("cache_mode"));
		props.setProperty(PluginManager.class, "cache.file",
				pluginsProperties.getProperty("cache_file"));
		props.setProperty(PluginManager.class, "logging.level",
				pluginsProperties.getProperty("cache_logging_level"));

		PluginManager manager = createPluginManager(props);
		String uriString;
		URI pluginsURI;
		File pluginsFile;
		uriString = pluginsProperties.getProperty("plugins_location", "");
		pluginsURI = pluginsProperties.getURIProperty("plugins_location", null);
		pluginsFile = pluginsProperties.getFileProperty("plugins_file", null);
		if (!isEmpty(uriString) && pluginsURI != null) {
			manager.addPluginsFrom(pluginsURI);
			log.debug("Add plugins from URI {}.", pluginsURI);
		}
		if (pluginsFile != null) {
			manager.addPluginsFrom(pluginsFile.toURI());
			log.debug("Add plugins from file {}.", pluginsFile);
		}
		return manager;
	}

	ContextProperties getPluginsProperties() throws URISyntaxException,
			IOException {
		Class<?> context = PluginsModule.class;
		ContextProperties system;
		ContextProperties defaultProperties;
		system = new ContextProperties(context, System.getProperties());
		URI resource = system.getURIProperty("plugins_properties_resource");
		File file = system.getFileProperty("plugins_properties_file");
		defaultProperties = new ContextPropertiesFactory(context)
				.fromResource(PLUGINS_PROPERTIES);

		if (resource != null) {
			return new ContextPropertiesFactory(context, defaultProperties)
					.fromResource(resource);
		} else if (file != null) {
			return new ContextPropertiesFactory(context, defaultProperties)
					.fromResource(file);
		} else {
			return defaultProperties;
		}
	}
}
