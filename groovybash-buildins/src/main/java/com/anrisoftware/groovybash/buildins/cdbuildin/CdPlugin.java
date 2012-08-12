/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
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
