package com.anrisoftware.groovybash.buildins.cdbuildin;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Add a binding to the {@code cd} build-in plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
public class CdPluginModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<BuildinPlugin> binder;
		binder = newSetBinder(binder(), BuildinPlugin.class);
		binder.addBinding().to(CdPlugin.class);
	}

}
