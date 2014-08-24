package com.anrisoftware.mongoose.devices.blkidbuildin;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Properties loaded from {@code "/blkid.properties"}
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
class BlkidBuildinProperties extends AbstractContextPropertiesProvider {

    private static final URL resource = BlkidBuildinProperties.class
            .getResource("/blkid.properties");

    BlkidBuildinProperties() {
        super(BlkidBuildinProperties.class, resource);
    }

}
