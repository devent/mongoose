package com.anrisoftware.mongoose.devices.api;

/**
 * Block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Block extends Device, Mountable, Resizeable {

    /**
     * Returns the label of the device.
     * 
     * @return the label of the device.
     */
    String getTheLabel();

    /**
     * Returns the unique identifier of the device.
     * 
     * @return the UUID of the device.
     */
    String getTheUUID();
}
