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
package com.anrisoftware.groovybash.core.buildins;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Provides a default of the the standard input and output streams for build-ins
 * commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BuildinsModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	/**
	 * Provides a default of the the standard input and output streams for
	 * build-ins commands.
	 * 
	 * @return the {@link StandardStreams} that returns the standard input and
	 *         output streams.
	 */
	@Provides
	protected StandardStreams getStandardStreams() {
		return new StandardStreams(System.in, System.out, System.err);
	}
}
