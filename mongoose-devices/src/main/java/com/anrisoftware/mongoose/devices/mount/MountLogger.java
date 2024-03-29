package com.anrisoftware.mongoose.devices.mount;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link Mount}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MountLogger extends AbstractLogger {

    private static final String MOUNT_PATH_SET = "Mount path '{}' set for {}.";
    private static final String MOUNT_MESSAGE = "Mount device '{}' on '{}'.";
    private static final String MOUNT = "Mount {} on '{}'.";
    private static final String DEVICE_MOUNTED = "Device %s already mounted on %s.";
    private static final String PATH_DIRECTORY = "Path '%s' must be directory.";
    private static final String PATH_NULL = "The path can not be null.";
    private static final String DEVICE_NOT_MOUNTED = "Device %s is not mounted on %s.";
    private static final String SET_DEVICE_PATH = "Set device path {} for {}.";
    private static final String DEVICE_PATH = "Need the device path.";
    private static final String path_unknown = "Mount path unknown";
    private static final String path_unknown_message = "Mount path unknown for '{}'.";
    private static final String MOUNT_ = "mount";

    /**
     * Create logger for {@link Mount}.
     */
    public MountLogger() {
        super(Mount.class);
    }

    void checkArgs(Mount mount, int size) {
        isTrue(size > 0, DEVICE_PATH);
    }

    void devicePathSet(Mount mount, File path) {
        if (log.isDebugEnabled()) {
            log.debug(SET_DEVICE_PATH, path, mount);
        } else {
            log.info(SET_DEVICE_PATH, path, mount.getTheName());
        }
    }

    void checkPath(Mount mount, File path) {
        notNull(path, PATH_NULL);
        isTrue(path.isDirectory(), PATH_DIRECTORY, path);
    }

    void checkNotMounted(Mount mount, boolean mounted, File path) {
        isTrue(!mounted, DEVICE_MOUNTED, mount.getTheDevice(), path);
    }

    void checkMounted(Mount mount, boolean mounted, File path) {
        isTrue(mounted, DEVICE_NOT_MOUNTED, mount.getTheDevice(), path);
    }

    void mountDevice(Mount mount, File path) {
        if (log.isDebugEnabled()) {
            log.debug(MOUNT, mount, path);
        } else {
            log.info(MOUNT_MESSAGE, mount.getTheDevice(), path);
        }
    }

    void mountPathSet(Mount mount, File path) {
        if (log.isDebugEnabled()) {
            log.debug(MOUNT_PATH_SET, path, mount);
        } else {
            log.info(MOUNT_PATH_SET, path, mount.getTheDevice());
        }
    }

    CommandException pathUnknown(Mount mount) {
        return logException(
                new CommandException(path_unknown).add(MOUNT_, mount),
                path_unknown_message, mount.getTheDevice());
    }
}
