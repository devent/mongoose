package com.anrisoftware.mongoose.threads;

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Factory to create the fixed thread pool properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface FixedThreadingPropertiesFactory {

	FixedThreadingProperties create(ContextProperties properties, String name);
}
