package com.anrisoftware.groovybash.core.environment;

import static java.lang.String.format;
import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.anrisoftware.groovybash.core.api.Environment;
import com.anrisoftware.groovybash.core.factories.BuildinPluginsLoaderFactory;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.common.collect.Maps;
import com.google.inject.Injector;

class EnvironmentImpl extends GroovyObjectSupport implements Environment {

	private final BuildinPluginsLoaderFactory loaderFactory;

	private final ContextProperties properties;

	private File workingDirectory;

	private final Map<String, BuildinPlugin> buildinPlugins;

	private Injector injector;

	@Inject
	EnvironmentImpl(@Named("environmentProperties") Properties properties,
			BuildinPluginsLoaderFactory loaderFactory) {
		super();
		this.properties = new ContextProperties(this, properties);
		this.loaderFactory = loaderFactory;
		this.workingDirectory = new File(".");
		this.buildinPlugins = Maps.newHashMap();
		loadBuildins();
	}

	private void loadBuildins() {
		List<String> pluginNames = getBuildinNames();
		List<BuildinPlugin> plugins = loadBuildinPlugins(pluginNames);
		for (BuildinPlugin plugin : plugins) {
			buildinPlugins.put(plugin.getName(), plugin);
		}
	}

	private List<String> getBuildinNames() {
		List<String> names = properties.getListProperty("buildin_commands");
		for (int i = 0; i < names.size(); i++) {
			names.set(i, format("buildin:%s", names.get(i)));
		}
		return names;
	}

	private List<BuildinPlugin> loadBuildinPlugins(List<String> pluginNames) {
		try {
			Callable<List<BuildinPlugin>> loaders = loaderFactory.create(
					BuildinPlugin.class, pluginNames);
			return loaders.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	@Override
	public void setWorkingDirectory(File directory) {
		workingDirectory = directory;
	}

	@Override
	public File getWorkingDirectory() {
		return workingDirectory;
	}

	@Override
	public File getUserHome() {
		return new File(System.getProperty("user.home"));
	}

	@Override
	public Object invokeMethod(String name, Object args) {
		Object[] uargs = InvokerHelper.asUnwrappedArray(args);
		if (getMetaClass().getMetaMethod(name, uargs) != null) {
			return super.invokeMethod(name, args);
		}
		Buildin buildin = buildinPlugins.get(name).getBuildin(injector);
		buildin.setEnvironment(this);
		buildin.setArguments(uargs);
		try {
			return buildin.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getProperty(String name) {
		if (getMetaClass().hasProperty(this, name) != null) {
			return super.getProperty(name);
		}
		return invokeMethod(name, null);
	}
}
