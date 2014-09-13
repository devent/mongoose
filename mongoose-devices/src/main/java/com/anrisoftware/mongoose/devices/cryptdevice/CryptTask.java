package com.anrisoftware.mongoose.devices.cryptdevice;

import java.io.IOException;
import java.util.Map;

import com.anrisoftware.mongoose.devices.device.AbstractDevice;

/**
 * Opens and closes crypt devices.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface CryptTask {

    /**
     * Sets the block device.
     * 
     * @param device
     *            the {@link AbstractDevice}.
     */
    void setDevice(CryptDevice cryptDevice);

    /**
     * @see CryptDevice#open(Map, String)
     */
    void open(Map<String, Object> args, String name) throws IOException;

    /**
     * @see CryptDevice#close(String)
     */
    void close(String name) throws IOException;

}
