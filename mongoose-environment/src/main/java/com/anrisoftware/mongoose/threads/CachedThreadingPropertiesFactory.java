package com.anrisoftware.mongoose.threads;

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Factory to create the cached thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface CachedThreadingPropertiesFactory {

	CachedThreadingProperties create(ContextProperties properties, String name);
}
