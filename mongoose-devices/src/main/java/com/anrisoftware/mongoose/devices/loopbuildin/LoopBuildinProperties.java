package com.anrisoftware.mongoose.devices.loopbuildin;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Properties loaded from {@code "/loop.properties"}
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
class LoopBuildinProperties extends AbstractContextPropertiesProvider {

    private static final URL resource = LoopBuildinProperties.class
            .getResource("/loop.properties");

    LoopBuildinProperties() {
        super(LoopBuildinProperties.class, resource);
    }

}
