package com.anrisoftware.mongoose.threads;

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Factory to create the single thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface SingleThreadingPropertiesFactory {

	SingleThreadingProperties create(ContextProperties properties, String name);
}
