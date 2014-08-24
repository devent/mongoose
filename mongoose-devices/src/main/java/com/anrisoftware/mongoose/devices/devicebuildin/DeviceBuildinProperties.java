package com.anrisoftware.mongoose.devices.devicebuildin;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Properties loaded from {@code "/device.properties"}
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
class DeviceBuildinProperties extends AbstractContextPropertiesProvider {

    private static final URL resource = DeviceBuildinProperties.class
            .getResource("/device.properties");

    DeviceBuildinProperties() {
        super(DeviceBuildinProperties.class, resource);
    }

}
