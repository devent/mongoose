package com.anrisoftware.mongoose.threads;

import com.anrisoftware.propertiesutils.ContextProperties;

interface ThreadingPropertiesFactory {

	ThreadingProperties create(ContextProperties properties, String name);
}
