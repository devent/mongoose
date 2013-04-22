package com.anrisoftware.mongoose.resources.threads;

import static java.util.concurrent.Executors.newCachedThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Cached thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class CachedThreadingProperties extends ThreadingProperties {

	/**
	 * Create the cached executor service.
	 * 
	 * @param factory
	 *            optional the {@link ThreadFactory}.
	 * 
	 * @return the cached {@link ExecutorService}.
	 */
	public ExecutorService createExecutorService(ThreadFactory factory) {
		return factory == null ? newCachedThreadPool()
				: newCachedThreadPool(factory);
	}
}
