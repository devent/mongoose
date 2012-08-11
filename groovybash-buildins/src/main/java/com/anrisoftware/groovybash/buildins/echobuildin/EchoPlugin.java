package com.anrisoftware.groovybash.buildins.echobuildin;

import com.anrisoftware.groovybash.core.Buildin;
import com.anrisoftware.groovybash.core.BuildinPlugin;
import com.google.inject.Injector;

/**
 * Returns the build-in command {@code echo}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class EchoPlugin implements BuildinPlugin {

	private Injector injector;

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "echo";
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
			injector = parentInjector.createChildInjector(new EchoModule());
		}
	}
}
