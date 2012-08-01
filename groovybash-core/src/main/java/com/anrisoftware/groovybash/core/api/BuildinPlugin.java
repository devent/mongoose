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
