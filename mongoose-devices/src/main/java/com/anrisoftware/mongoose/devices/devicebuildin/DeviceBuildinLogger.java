package com.anrisoftware.mongoose.devices.devicebuildin;

import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.args_null;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.device_null;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.device_path_null;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.device_path_set;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.device_set;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.device_type;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.loop_path_null;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.loop_path_set;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.syntax_exception;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.syntax_exception_message;
import static com.anrisoftware.mongoose.devices.devicebuildin.DeviceBuildinLogger._.type_set;
import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * Logging messages for {@link DeviceBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DeviceBuildinLogger extends AbstractLogger {

    enum _ {

        device_set("Set device '{}' to {}."),

        device_type("Device type needs to be set for %s."),

        device_null("Device can not be null."),

        device_path_null("Device path can not be null for %s."),

        args_null("Needs at least one device path."),

        device_path_set("Set device path '{}' to {}."),

        type_set("Device type '{}' set for {}."),

        loop_path_null("Loop device path can not be null or empty for %s."),

        loop_path_set("Loop device '{}' set for {}."),

        syntax_exception("URI syntax for '%s' is invalid for device %s."),

        syntax_exception_message(
                "URI syntax for '{}' is invalid for device {}.");

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
     * Create logger for {@link DeviceBuildin}.
     */
    DeviceBuildinLogger() {
        super(DeviceBuildin.class);
    }

    void checkType(DeviceBuildin buildin, String scheme) {
        notBlank(scheme, device_type.toString(), buildin);
    }

    void typeSet(DeviceBuildin buildin, String type) {
        if (isDebugEnabled()) {
            debug(type_set, type, buildin);
        } else {
            info(type_set, type, buildin.getTheName());
        }
    }

    void checkArgs(AbstractCommand buildin, int size) {
        isTrue(size > 0, args_null.toString());
    }

    void checkDevice(AbstractCommand buildin, URI device) {
        notNull(device, device_null.toString());
    }

    void deviceSet(DeviceBuildin buildin, File device) {
        if (isDebugEnabled()) {
            debug(device_set, device, buildin);
        } else {
            info(device_set, device, buildin.getTheName());
        }
    }

    void checkDevicePath(DeviceBuildin buildin, Object path) {
        notNull(path, device_path_null.toString(), buildin);
    }

    void devicePathSet(DeviceBuildin buildin, File device) {
        if (isDebugEnabled()) {
            debug(device_path_set, device, buildin);
        } else {
            info(device_path_set, device, buildin.getTheName());
        }
    }

    void checkLoopPath(DeviceBuildin buildin, String path) {
        notBlank(path, loop_path_null.toString(), buildin);
    }

    void loopPathSet(DeviceBuildin buildin, String path) {
        if (isDebugEnabled()) {
            debug(loop_path_set, path, buildin);
        } else {
            info(loop_path_set, path, buildin.getTheName());
        }
    }

    IllegalArgumentException syntaxException(DeviceBuildin buildin,
            URISyntaxException e, Object path) {
        return logException(
                new IllegalArgumentException(
                        format(syntax_exception.toString(), path,
                                buildin.getTheName()), e),
                syntax_exception_message, path, buildin.getTheName());
    }

}
