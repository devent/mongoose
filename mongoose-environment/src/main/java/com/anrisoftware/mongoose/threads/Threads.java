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
package com.anrisoftware.mongoose.threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.joda.time.Duration;

/**
 * Submits tasks for execution.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 */
public interface Threads extends ExecutorService {

	/**
	 * Sets the name of the threads pools. The properties of the thread pool are
	 * loaded and a new pool is created.
	 * 
	 * @param name
	 *            the {@link String} name.
	 * 
	 * @throws ThreadsException
	 *             if there was any error load the property of the thread pool.
	 * 
	 * @throws NullPointerException
	 *             if the specified name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	void setName(String name) throws ThreadsException;

	/**
	 * Returns the name of the threads pool.
	 * 
	 * @return the {@link String} name.
	 */
	String getName();

	/**
	 * Waits for all tasks to finish.
	 * 
	 * @throws InterruptedException
	 *             if interrupted while waiting.
	 */
	void waitForTasks() throws InterruptedException;

	/**
	 * Waits for all tasks to finish up until the specified timeout.
	 * 
	 * @param timeout
	 *            the {@link Duration} timeout to wait for one task to finish.
	 * 
	 * @return the tasks {@link List} that was not finished before the timeout.
	 * 
	 * @throws InterruptedException
	 *             if interrupted while waiting.
	 */
	List<Future<?>> waitForTasks(Duration timeout) throws InterruptedException;
}