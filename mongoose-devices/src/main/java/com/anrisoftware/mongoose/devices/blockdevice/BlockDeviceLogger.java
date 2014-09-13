package com.anrisoftware.mongoose.devices.blockdevice;

import static com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceLogger._.unsupported_file_system;
import static com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceLogger._.unsupported_file_system_message;
import static java.lang.String.format;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link BlockDevice}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BlockDeviceLogger extends AbstractLogger {

    enum _ {

        unsupported_file_system_message(
                "The file system of device {} is not supported."),

        unsupported_file_system("File system is not supported for %s");

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
     * Sets the context of the logger to {@link BlockDevice}.
     */
    public BlockDeviceLogger() {
        super(BlockDevice.class);
    }

    UnsupportedOperationException unsupportedFileSystem(BlockDevice device) {
        return logException(
                new UnsupportedOperationException(format(
                        unsupported_file_system.toString(), device)),
                unsupported_file_system_message, device.getThePath());
    }
}
