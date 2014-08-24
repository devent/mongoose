package com.anrisoftware.mongoose.devices.devicebuildin;

import static java.lang.String.format;
import static org.codehaus.groovy.runtime.InvokerHelper.getProperty;
import groovy.lang.MetaClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Device;
import com.anrisoftware.mongoose.devices.blockdevice.BlockDevice;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Maps a device path to a device.
 * 
 * @see BlockDevice
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DeviceBuildin extends AbstractCommand {

    private static final String DEVICE = "device";

    private static final String FILE_COMMAND_PROPERTY = "file_command";

    private static final String TYPE_KEY = "type";

    private static final String LOOP_COMMAND = "loop";

    private static final String EXEC_COMMAND = "exec";

    private static final String LOOP_TYPE = "loop";

    private static final String LOOP_KEY = "loop";

    private final DeviceBuildinLogger log;

    private final CommandLoader loader;

    private String fileCommand;

    private List<Object> deviceArgs;

    private String type;

    private Device device;

    private File devicePath;

    private String loop;

    private MetaClass deviceMetaclass;

    @Inject
    DeviceBuildin(DeviceBuildinLogger logger, CommandLoader loader,
            @Named("device-properties") ContextProperties p) {
        this.log = logger;
        this.loader = loader;
        this.device = null;
        this.devicePath = null;
        this.fileCommand = p.getProperty(FILE_COMMAND_PROPERTY);
    }

    @Override
    public void setEnvironment(Environment environment) throws CommandException {
        super.setEnvironment(environment);
        if (environment.getEnv().containsKey("FILE_COMMAND")) {
            fileCommand = environment.getEnv().get("FILE_COMMAND");
        }
    }

    @Override
    protected void doCall() throws Exception {
        if (!LOOP_TYPE.equals(type) && !isImageFile(devicePath)) {
            createLoDevice(devicePath);
        }
        Command cmd = createDevice(type);
        this.device = (Device) cmd;
        this.deviceMetaclass = InvokerHelper.getMetaClass(device);
        getTheEnvironment().executeCommandAndWait(cmd);
    }

    private boolean isImageFile(File path) throws CommandException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String command = format("%s %s", fileCommand, path);
        Command cmd = loader.createCommand(EXEC_COMMAND, getTheEnvironment(),
                getArgs(), output, getError(), getInput(), command);
        getTheEnvironment().executeCommandAndWait(cmd);
        String outstr = output.toString();
        return outstr.matches(".+block special");
    }

    private void createLoDevice(File path) throws Exception {
        List<Object> args = new ArrayList<Object>();
        args.add(path);
        if (loop != null) {
            args.add(loop);
        }
        Command cmd = loader.createCommand(LOOP_COMMAND, getTheEnvironment(),
                getArgs(), getOutput(), getError(), getInput(), args.toArray());
        getTheEnvironment().executeCommandAndWait(cmd);
        File device = (File) getProperty(cmd, "theDevice");
        deviceArgs.set(0, device);
    }

    private Command createDevice(String type) throws CommandException {
        return loader.createCommand(type, getTheEnvironment(), getArgs(),
                getOutput(), getError(), getInput(), deviceArgs.toArray());
    }

    @Override
    protected void argumentsSet(Map<String, Object> args,
            List<Object> unnamedArgs) throws Exception {
        if (args.containsKey(TYPE_KEY)) {
            setType(args.get(TYPE_KEY).toString());
        }
        if (args.containsKey(LOOP_KEY)) {
            setLoop(args.get(LOOP_KEY).toString());
        }
        log.checkArgs(this, unnamedArgs.size());
        this.deviceArgs = new ArrayList<Object>(unnamedArgs);
        setupUnnamed(unnamedArgs);
    }

    private void setupUnnamed(List<Object> args) {
        setDevice(args.get(0));
    }

    /**
     * Sets the device type.
     * 
     * @param type
     *            the device {@link String} type.
     * 
     * @throws NullPointerException
     *             if the specified type is {@code null}.
     * 
     * @throws IllegalArgumentException
     *             if the specified type is empty.
     */
    public void setType(String type) {
        log.checkType(this, type);
        this.type = type;
        log.typeSet(this, type);
    }

    /**
     * Returns the device type.
     * 
     * @return the device {@link String} type.
     */
    public String getTheType() {
        return type;
    }

    /**
     * Sets the device.
     * 
     * @param path
     *            path or name of the loop device.
     * 
     * @throws NullPointerException
     *             if the specified path is {@code null}.
     * 
     * @throws IllegalArgumentException
     *             if the specified path is empty.
     */
    public void setLoop(String path) {
        log.checkLoopPath(this, path);
        this.loop = path;
        log.loopPathSet(this, path);
    }

    /**
     * Sets the device.
     * 
     * @param path
     *            the {@link URI} of the device; if no device type was set the
     *            URI's scheme is used. Or the {@link File} path of the device.
     * 
     * @throws NullPointerException
     *             if the specified path is {@code null}.
     * 
     * @throws IllegalArgumentException
     *             if the device URI does not have a scheme and the type was not
     *             set.
     */
    public void setDevice(Object path) {
        log.checkDevicePath(this, path);
        String scheme = null;
        File file = null;
        if (path instanceof URI) {
            URI uri = (URI) path;
            scheme = uri.getScheme();
            file = new File(uri.getPath());
        } else if (path instanceof File) {
            scheme = null;
            file = (File) path;
        } else {
            setDevice(createURI(path));
            return;
        }
        if (type == null) {
            setType(scheme);
        }
        this.devicePath = file;
        deviceArgs.set(0, file);
        log.deviceSet(this, file);
    }

    private URI createURI(Object path) {
        try {
            return new URI(path.toString());
        } catch (URISyntaxException e) {
            throw log.syntaxException(this, e, path);
        }
    }

    /**
     * Returns the device.
     * 
     * @return the device {@link Device}.
     */
    public Device getTheDevice() {
        return device;
    }

    @Override
    public String getTheName() {
        return DeviceBuildinService.ID;
    }

    /**
     * Delegates the method to the device.
     */
    public Object methodMissing(String name, Object args) {
        return deviceMetaclass.invokeMethod(device, name, args);
    }

    /**
     * Delegates the property access to the device.
     */
    public Object propertyMissing(String name) {
        return deviceMetaclass.getProperty(device, name);
    }

    /**
     * Delegates the property access to the device.
     */
    public void propertyMissing(String name, Object value) {
        deviceMetaclass.setProperty(device, name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append(DEVICE, device).toString();
    }
}
