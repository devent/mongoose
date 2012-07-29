package com.anrisoftware.groovybash.core.api;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.Capabilities;

import com.google.inject.Injector;

/**
 * Returns a Groovy Bash build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BuildinPlugin extends Plugin {

	/**
	 * Returns the plug-in capabilities.
	 * 
	 * @return the capabilities.
	 */
	@Capabilities
	String[] getCapabilities();

	/**
	 * Returns the build-in.
	 * 
	 * @param injector
	 *            the {@link Injector} to create the build-in.
	 * 
	 * @return the {@link Buildin}.
	 */
	Buildin getBuildin(Injector injector);

	/**
	 * Returns the name of the build-in command.
	 * 
	 * @return the name of the build-in command.
	 */
	String getName();

}
