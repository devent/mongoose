package com.anrisoftware.mongoose.resources.threads;

import com.anrisoftware.propertiesutils.ContextProperties;

interface ThreadingPropertiesFactory {

	ThreadingProperties create(ContextProperties properties, String name);
}
