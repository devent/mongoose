package com.anrisoftware.mongoose.threads;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Single thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class SingleThreadingProperties extends ThreadingProperties {

	/**
	 * @see SingleThreadingPropertiesFactory#create(ContextProperties, String)
	 */
	@Inject
	SingleThreadingProperties(@Assisted ContextProperties p,
			@Assisted String name) {
		super(p, name);
	}

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
