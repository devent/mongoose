package com.anrisoftware.mongoose.threads;

import static java.util.concurrent.Executors.newCachedThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Cached thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class CachedThreadingProperties extends ThreadingProperties {

	/**
	 * @see CachedThreadingPropertiesFactory#create(ContextProperties, String)
	 */
	@Inject
	CachedThreadingProperties(@Assisted ContextProperties p,
			@Assisted String name) {
		super(p, name);
	}

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
