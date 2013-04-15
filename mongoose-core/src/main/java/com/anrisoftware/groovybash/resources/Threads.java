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
package com.anrisoftware.groovybash.resources;

import java.util.concurrent.Future;

/**
 * Submits tasks for execution.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 */
public interface Threads {

	/**
	 * Submits a task for execution.
	 * 
	 * @param task
	 *            the {@link Runnable} task.
	 * 
	 * @return the {@link Future} representing that task.
	 */
	Future<?> submitTask(Runnable task);

	/**
	 * Shutdown the executor service.
	 */
	void shutdown();

}
