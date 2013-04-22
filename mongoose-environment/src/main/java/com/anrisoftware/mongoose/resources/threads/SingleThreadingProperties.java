package com.anrisoftware.mongoose.resources.threads;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Single thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class SingleThreadingProperties extends ThreadingProperties {

	/**
	 * Create the single executor service.
	 * 
	 * @param factory
	 *            optional the {@link ThreadFactory}.
	 * 
	 * @return the single {@link ExecutorService}.
	 */
	public ExecutorService createExecutorService(ThreadFactory factory) {
		return factory == null ? newSingleThreadExecutor()
				: newSingleThreadExecutor(factory);
	}
}
