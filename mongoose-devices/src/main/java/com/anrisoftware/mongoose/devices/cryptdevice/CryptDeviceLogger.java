package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.container_not_open;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.container_not_open_message;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.the_device;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.unsupported_crypt_message;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.unsupported_crypt_system;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.unsupported_size_unit;
import static com.anrisoftware.mongoose.devices.cryptdevice.CryptDeviceLogger._.unsupported_size_unit_message;
import static java.lang.String.format;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging for {@link CryptDevice}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CryptDeviceLogger extends AbstractLogger {

    enum _ {

        unsupported_crypt_message("The crypt device {} is not supported."),

        unsupported_crypt_system("Crypt device {} is not supported"),

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
     * Sets the context of the logger to {@link CryptDevice}.
     */
    public CryptDeviceLogger() {
        super(CryptDevice.class);
    }

    UnsupportedOperationException unsupportedFileSystem(CryptDevice device) {
        return logException(
                new UnsupportedOperationException(format(
                        unsupported_crypt_system.toString(), device)),
                unsupported_crypt_message, device.getThePath());
    }

    CommandException unsupportedSize(CryptDevice device, Object unit) {
        return logException(new CommandException(unsupported_size_unit).add(
                the_device, device), unsupported_size_unit_message, unit,
                device.getThePath());
    }

    void checkDeviceOpen(CryptDevice device, boolean status)
            throws CommandException {
        if (!status) {
            throw logException(new CommandException(container_not_open).add(
                    the_device, device), container_not_open_message,
                    device.getThePath());
        }
    }
}
