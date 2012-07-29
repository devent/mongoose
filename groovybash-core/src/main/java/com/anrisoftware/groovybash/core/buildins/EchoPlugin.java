package com.anrisoftware.groovybash.core.buildins;

import static java.lang.String.format;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Returns the echo build-in as a plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@PluginImplementation
class EchoPlugin implements BuildinPlugin {

	@Override
	public String[] getCapabilities() {
		return new String[] { format("buildin:%s", getName()) };
	}

	@Override
	public Buildin getBuildin(Injector injector) {
		Iterable<? extends Module> modules = Lists.newArrayList();
		Injector childInjector = injector.createChildInjector(modules);
		return childInjector.getInstance(Buildin.class);
	}

	@Override
	public String getName() {
		return "echo";
	}

}
