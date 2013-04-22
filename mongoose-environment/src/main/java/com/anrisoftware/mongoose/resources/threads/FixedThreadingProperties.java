package com.anrisoftware.mongoose.resources.threads;

import static java.lang.String.format;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

/**
 * Fixed thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class FixedThreadingProperties extends ThreadingProperties {

	private static final String MAX_THREADS_PROPERTY = "max_threads";

	private final FixedThreadingPropertiesLogger log;

	@Inject
	FixedThreadingProperties(FixedThreadingPropertiesLogger logger) {
		this.log = logger;
	}

	/**
	 * Returns the maximum number of threads for the pool.
	 * 
	 * @return the maximum number of threads.
	 * 
	 * @throws NullPointerException
	 *             if no maximum number of threads property was found.
	 */
	public int getMaxThreads() {
		Number value = getProperties().getNumberProperty(
				format(KEY_TEMPLATE, MAX_THREADS_PROPERTY));
		log.checkMaxThreads(this, value);
		return value.intValue();
	}

	/**
	 * Returns the maximum number of threads for the pool.
	 * 
	 * @param defaultValue
	 *            the default maximum number of threads.
	 * 
	 * @return the maximum number of threads or the default value.
	 */
	public int getMaxThreads(int defaultValue) {
		Number value = getProperties().getNumberProperty(
				format(KEY_TEMPLATE, MAX_THREADS_PROPERTY), defaultValue);
		return value.intValue();
	}

	/**
	 * Create the fixed executor service.
	 * 
	 * @param factory
	 *            optional the {@link ThreadFactory}.
	 * 
	 * @param maxThreads
	 *            maximum number of threads in the pool.
	 * 
	 * @return the fixed {@link ExecutorService}.
	 */
	public ExecutorService createExecutorService(ThreadFactory factory,
			int maxThreads) {
		return factory == null ? newFixedThreadPool(maxThreads)
				: newFixedThreadPool(maxThreads, factory);
	}
}
