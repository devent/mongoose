package com.anrisoftware.groovybash.core;

import com.google.inject.Injector;

/**
 * Returns the build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.2
 */
public interface BuildinPlugin {

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the name.
	 */
	String getName();

	/**
	 * Returns the build-in command.
	 * 
	 * @param parentInjector
	 *            the parent {@link Injector}.
	 * 
	 * @return the {@link Buildin}.
	 */
	Buildin getBuildin(Injector parentInjector);
}
