package com.anrisoftware.groovybash.buildins.logbuildins;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Add a binding to the logging build-in plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class LogPluginModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<BuildinPlugin> binder;
		binder = newSetBinder(binder(), BuildinPlugin.class);
		binder.addBinding().to(DebugPlugin.class);
		binder.addBinding().to(ErrorPlugin.class);
		binder.addBinding().to(InfoPlugin.class);
		binder.addBinding().to(TracePlugin.class);
		binder.addBinding().to(WarnPlugin.class);
	}

}
