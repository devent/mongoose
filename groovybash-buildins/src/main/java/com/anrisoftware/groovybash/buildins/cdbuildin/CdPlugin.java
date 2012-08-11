package com.anrisoftware.groovybash.buildins.cdbuildin;

import com.anrisoftware.groovybash.core.Buildin;
import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.google.inject.Injector;

/**
 * Returns the build-in command {@code cd}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class CdPlugin implements BuildinPlugin {

	private Injector injector;

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the name {@code cd}.
	 */
	@Override
	public String getName() {
		return "cd";
	}

	/**
	 * Returns the build-in command.
	 * 
	 * @return the {@link Buildin}.
	 */
	@Override
	public Buildin getBuildin(Injector parentInjector) {
		lazyCreateInjector(parentInjector);
		return injector.getInstance(Buildin.class);
	}

	private void lazyCreateInjector(Injector parentInjector) {
		if (injector == null) {
			injector = parentInjector.createChildInjector(new CdModule());
		}
	}
}
