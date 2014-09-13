package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.MountTaskLogger._.container_not_open;
import static com.anrisoftware.mongoose.devices.cryptdevice.MountTaskLogger._.container_not_open_message;
import static com.anrisoftware.mongoose.devices.cryptdevice.MountTaskLogger._.the_device;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging for {@link MountTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MountTaskLogger extends AbstractLogger {

    enum _ {

        container_not_open("Encrypted container is not open"),

        container_not_open_message("Encrypted container '{}' is not open."),

        the_device("device");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link MountTask}.
     */
    public MountTaskLogger() {
        super(MountTask.class);
    }

    void checkOpenedName(CryptDevice device, String name)
            throws CommandException {
        if (name == null) {
            throw logException(new CommandException(container_not_open).add(
                    the_device, device), container_not_open_message,
                    device.getThePath());
        }
    }
}
