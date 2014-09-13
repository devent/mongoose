package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.Utils.getVar;
import static java.lang.String.format;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceUnits;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Manage resizeables for opened crypt containers.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ResizeTask {

    private static final String SUDO_COMMAND = "sudo";

    private static final String CRYPTSETUP_PROPERTY = "cryptsetup_command";

    @Inject
    private ResizeTaskLogger log;

    @Inject
    private CryptDevicePropertiesProvider properties;

    @Inject
    private CommandLoader loader;

    private CryptDevice device;

    private Environment environment;

    private String cryptsetupCommand;

    /**
     * Probes the environment variables:
     * <dl>
     * 
     * <dt>{@link #CRYPTSETUP_PROPERTY}</dt>
     * <dd>the command to open and close LUKS encrypted container,
     * {@code cryptsetup}</dd>
     * 
     * </dl>
     */
    public void setDevice(CryptDevice device) {
        this.device = device;
        this.environment = device.getTheEnvironment();
        setupProperties(environment.getEnv(), properties.get());
    }

    public void resize(long size, Object unit) throws IOException {
        size = getSize(size, unit);
        String name = device.getTheFirstOpen();
        log.checkOpenedName(device, name);
        Command cmd = loader
                .createCommand(
                        SUDO_COMMAND,
                        device.getTheEnvironment(),
                        device.getArgs(),
                        device.getOutput(),
                        device.getError(),
                        device.getInput(),
                        format("%s resize --size %d %s", cryptsetupCommand,
                                size, name));
        environment.executeCommandAndWait(cmd);
    }

    private long getSize(long size, Object unit) throws CommandException {
        BlockDeviceUnits units = (BlockDeviceUnits) unit;
        switch (units) {
        case SECTORS:
            return size;
        case BYTE_SIZE:
            return size / 512;
        default:
            throw log.unsupportedSize(device, unit);
        }
    }

    private void setupProperties(Map<String, String> env, ContextProperties p) {
        this.cryptsetupCommand = getVar(env, p, CRYPTSETUP_PROPERTY);
    }

}
