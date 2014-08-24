package com.anrisoftware.mongoose.devices.blkidbuildin;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;

/**
 * Execute the {@code blkid} command on a device path.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BlkidBuildin extends AbstractCommand {

    private static final String S_S = "%s %s";

    private static final String SUDO_NAME = "sudo";

    private static final String PATH = "path";

    private static final String BLKID_NAME = "blkid";

    private static final String LABEL_KEY = "LABEL";

    private static final String UUID_KEY = "UUID";

    private static final String TYPE_KEY = "TYPE";

    private static final String SEC_TYPE_KEY = "SEC_TYPE";

    private static final String BLKID_COMMAND_PROPERTY = "blkid_command";

    private static final String BLKID_COMMAND_KEY = "BLKID_COMMAND";

    private final Map<String, String> values;

    @Inject
    private BlkidBuildinLogger log;

    @Inject
    private CommandLoader loader;

    @Inject
    private BlkidParser parser;

    private String blkidCommand;

    private Command cmd;

    private File path;

    @Inject
    BlkidBuildin(BlkidBuildinProperties p) {
        this.blkidCommand = p.get().getProperty(BLKID_COMMAND_PROPERTY);
        this.values = new HashMap<String, String>();
    }

    @Override
    public void setEnvironment(Environment environment) throws CommandException {
        super.setEnvironment(environment);
        Map<String, String> env = environment.getEnv();
        if (env.containsKey(BLKID_COMMAND_KEY)) {
            this.blkidCommand = env.get(BLKID_COMMAND_KEY);
        }
    }

    @Override
    protected void doCall() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.cmd = loader.createCommand(SUDO_NAME, getTheEnvironment(),
                getArgs(), output, getError(), getInput(),
                format(S_S, blkidCommand, path));
        getTheEnvironment().executeCommandAndWait(cmd);
        this.parser.withDevice(path).withString(output.toString())
                .withValues(values).build();
    }

    @Override
    protected void argumentsSet(Map<String, Object> args,
            List<Object> unnamedArgs) throws Exception {
        log.checkArgs(this, unnamedArgs.size());
        setPath(new File(unnamedArgs.get(0).toString()));
    }

    /**
     * Sets the device path.
     * 
     * @param path
     *            the device {@link File} path.
     * 
     * @throws NullPointerException
     *             if the specified path is {@code null}.
     */
    public void setPath(File path) {
        log.checkDevicePath(this, path);
        this.path = path;
        log.devicePathSet(this, path);
    }

    /**
     * Returns the device path.
     * 
     * @return the device {@link File} path.
     */
    public File getThePath() {
        return path;
    }

    /**
     * Returns the secondary type of the device.
     * 
     * @return the device {@link String} secondary type, or {@code null}.
     */
    public String getTheSecondaryType() {
        return values.get(SEC_TYPE_KEY);
    }

    /**
     * Returns the type of the device.
     * 
     * @return the device {@link String} type, or {@code null}.
     */
    public String getTheType() {
        return values.get(TYPE_KEY);
    }

    /**
     * Returns the UUID of the device.
     * 
     * @return the device {@link String} UUID, or {@code null}.
     */
    public String getTheUUID() {
        return values.get(UUID_KEY);
    }

    /**
     * Returns the label of the device.
     * 
     * @return the device {@link String} label, or {@code null}.
     */
    public String getTheLabel() {
        return values.get(LABEL_KEY);
    }

    @Override
    public String getTheName() {
        return BLKID_NAME;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append(PATH, path).append(values).toString();
    }
}
