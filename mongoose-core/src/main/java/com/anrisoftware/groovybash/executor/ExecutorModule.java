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
package com.anrisoftware.groovybash.executor;

import javax.inject.Singleton;

import com.anrisoftware.groovybash.core.ExecutorServiceHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Provides the executor service handler.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class ExecutorModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	public ExecutorServiceHandler getExecutorServiceHandler() {
		return new FixedThreadPoolExecutorServiceHandler(4);
	}
}
