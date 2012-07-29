package com.anrisoftware.groovybash.core.buildins;

import static java.lang.String.format;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.google.inject.Injector;

/**
 * Returns the echo build-in as a plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@PluginImplementation
public class EchoPlugin implements BuildinPlugin {

	@Override
	@Capabilities
	public String[] getCapabilities() {
		return new String[] { format("buildin:%s", getName()) };
	}

	@Override
	public Buildin getBuildin(Injector injector) {
		Injector childInjector = injector.createChildInjector(new EchoModule());
		return childInjector.getInstance(Buildin.class);
	}

	@Override
	public String getName() {
		return "echo";
	}

}
