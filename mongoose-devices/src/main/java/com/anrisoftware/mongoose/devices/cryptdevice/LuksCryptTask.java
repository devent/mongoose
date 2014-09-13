package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.Utils.getVar;
import static java.lang.String.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Opens and closes LUKS crypt devices.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LuksCryptTask implements CryptTask {

    private static final String CRYPTSETUP_PROPERTY = "cryptsetup_command";

    private static final String SUDO_COMMAND = "sudo";

    private static final String PASSPHRASE_KEY = "passphrase";

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
    @Override
    public void setDevice(CryptDevice device) {
        this.device = device;
        this.environment = device.getTheEnvironment();
        setupProperties(environment.getEnv(), properties.get());
    }

    @Override
    public void open(Map<String, Object> args, String name) throws IOException {
        InputStream input = device.getInput();
        if (args.containsKey(PASSPHRASE_KEY)) {
            input = createPassphrase(args);
        }
        Command cmd = loader.createCommand(
                SUDO_COMMAND,
                environment,
                device.getArgs(),
                device.getOutput(),
                device.getError(),
                input,
                format("%s luksOpen %s %s", cryptsetupCommand,
                        device.getThePath(), name));
        environment.executeCommandAndWait(cmd);
    }

    @Override
    public void close(String name) throws IOException {
        Command cmd = loader.createCommand(SUDO_COMMAND, environment,
                device.getArgs(), device.getOutput(), device.getError(),
                device.getInput(),
                format("%s luksClose %s", cryptsetupCommand, name));
        environment.executeCommandAndWait(cmd);
    }

    private void setupProperties(Map<String, String> env, ContextProperties p) {
        this.cryptsetupCommand = getVar(env, p, CRYPTSETUP_PROPERTY);
    }

    private InputStream createPassphrase(Map<String, Object> args) {
        String passphrase = args.get(PASSPHRASE_KEY).toString();
        if (!passphrase.endsWith("\n")) {
            passphrase = passphrase + "\n";
        }
        return new ByteArrayInputStream(passphrase.getBytes());
    }

}
