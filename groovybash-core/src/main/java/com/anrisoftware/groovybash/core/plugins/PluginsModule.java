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

import static com.google.common.io.Resources.getResource;
import static java.lang.String.format;
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

	private static final String PACKAGE_NAME = PluginsModule.class.getPackage()
			.getName();

	private static final String PROPERTIES_RESOURCE_NAME = "plugins_properties_resource";

	/**
	 * The name of the system property for the URL location of the plug-ins
	 * properties file.
	 */
	public static final String PLUGINS_PROPERTIES_RESOURCE = format("%s.%s",
			PROPERTIES_RESOURCE_NAME, PACKAGE_NAME);

	private static final String PROPERTIES_FILE_NAME = "plugins_properties_file";

	/**
	 * The name of the system property for the file path of the plug-ins
	 * properties file.
	 */
	public static final String PLUGINS_PROPERTIES_FILE = format("%s.%s",
			PROPERTIES_FILE_NAME, PACKAGE_NAME);

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

	/**
	 * <p>
	 * Provides the plug-in manager. The manager is setup with the loaded
	 * properties.
	 * <p>
	 * We can set the properties file location with the system properties
	 * {@code com.anrisoftware.groovybash.core.plugins.plugins_location} or
	 * {@code com.anrisoftware.groovybash.core.plugins.plugins_file}. The
	 * location set a URI and file sets a file path to the properties file.
	 * 
	 * <dl>
	 * <dt>{@code com.anrisoftware.groovybash.core.plugins.cache_enabled}</dt>
	 * <dd>
	 * Can be set to {@code true} or {@code false}. Enabled or disables the
	 * cache for plug-ins. Defaults to {@code true}.</dd>
	 * 
	 * <dt>{@code com.anrisoftware.groovybash.core.plugins.cache_mode}</dt>
	 * <dd>
	 * Can be set to {@code weak} or {@code strong}. Defaults to {@code weak}.</dd>
	 * 
	 * <dt>{@code com.anrisoftware.groovybash.core.plugins.cache_file}</dt>
	 * <dd>
	 * The plug-ins cache file. Defaults to {@code jspf.cache}.</dd>
	 * 
	 * <dt>{@code com.anrisoftware.groovybash.core.plugins.cache_logging_level}</dt>
	 * <dd>
	 * Can be set to {@code OFF}, {@code WARNING}, {@code INFO}, {@code FINE},
	 * {@code FINER} or {@code FINEST}. The logging level for the plug-ins
	 * manager. Defaults to {@code WARNING}.</dd>
	 * </dl>
	 * 
	 * @return the {@link PluginManager}.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Provides
	public PluginManager getPluginManager() throws URISyntaxException,
			IOException {
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

	private ContextProperties getPluginsProperties() throws URISyntaxException,
			IOException {
		Class<?> context = PluginsModule.class;
		ContextProperties system;
		ContextProperties defaultProperties;
		system = new ContextProperties(context, System.getProperties());
		URI resource = system.getURIProperty(PROPERTIES_RESOURCE_NAME);
		File file = system.getFileProperty(PROPERTIES_FILE_NAME);
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
