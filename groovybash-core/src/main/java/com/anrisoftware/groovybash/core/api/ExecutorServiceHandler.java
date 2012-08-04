package com.anrisoftware.groovybash.core.api;

import java.util.concurrent.Future;

public interface ExecutorServiceHandler {

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
