package com.anrisoftware.mongoose.devices.cryptdevice;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Returns the properties from {@code "/cryptdevice.properties"}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class CryptDevicePropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL resource = CryptDevicePropertiesProvider.class
            .getResource("/cryptdevice.properties");

    CryptDevicePropertiesProvider() {
        super(CryptDevicePropertiesProvider.class, resource);
    }

}
