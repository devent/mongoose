package com.anrisoftware.mongoose.devices.cryptdevice;


import static com.anrisoftware.mongoose.devices.cryptdevice.ResizeTaskLogger._.container_not_open;
import static com.anrisoftware.mongoose.devices.cryptdevice.ResizeTaskLogger._.container_not_open_message;
import static com.anrisoftware.mongoose.devices.cryptdevice.ResizeTaskLogger._.the_device;
import static com.anrisoftware.mongoose.devices.cryptdevice.ResizeTaskLogger._.unsupported_size_unit;
import static com.anrisoftware.mongoose.devices.cryptdevice.ResizeTaskLogger._.unsupported_size_unit_message;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging for {@link ResizeTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ResizeTaskLogger extends AbstractLogger {

    enum _ {

        unsupported_size_unit("Unsupported device unit"),

        unsupported_size_unit_message(
                "Unsupported device unit '{}' for device '{}'."),

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
     * Sets the context of the logger to {@link ResizeTask}.
     */
    public ResizeTaskLogger() {
        super(ResizeTask.class);
    }

    void checkOpenedName(CryptDevice device, String name)
            throws CommandException {
        if (name == null) {
            throw logException(new CommandException(container_not_open).add(
                    the_device, device), container_not_open_message,
                    device.getThePath());
        }
    }

    CommandException unsupportedSize(CryptDevice device, Object unit) {
        return logException(new CommandException(unsupported_size_unit).add(
                the_device, device), unsupported_size_unit_message, unit,
                device.getThePath());
    }

}
