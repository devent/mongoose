package com.anrisoftware.mongoose.devices.blockdevice;

import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;

/**
 * Resize the block device file system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface ResizeTask {

    /**
     * Sets the block device.
     * 
     * @param device
     *            the {@link AbstractDevice}.
     */
    void setDevice(AbstractDevice device);

    /**
     * @see BlockDevice#size(Object)
     */
    long size(Object unit) throws CommandException;

    /**
     * @see BlockDevice#resize(long, Object)
     */
    void resize(long size, Object unit) throws CommandException;

}
