package com.anrisoftware.mongoose.devices.cryptdevice;

import static org.codehaus.groovy.runtime.InvokerHelper.getProperty;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Block;
import com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceUnits;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;

/**
 * Crypt device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CryptDevice extends AbstractDevice implements Block {

    private static final String UUID_KEY = "uuid";

    private static final String LABEL_KEY = "label";

    private static final String TYPE_KEY = "type";

    private static final String UUID = "uuid";

    private static final String LABEL = "label";

    private static final String THE_UUID_KEY = "theUUID";

    private static final String THE_LABEL_KEY = "theLabel";

    private static final String THE_TYPE_KEY = "theType";

    private static final String BLKID_COMMAND = "blkid";

    @Inject
    private CryptDeviceLogger log;

    @Inject
    private StatusTaskFactory statusTaskFactory;

    @Inject
    private CommandLoader loader;

    @Inject
    private MountTask mountable;

    @Inject
    private ResizeTask resizeable;

    @Inject
    private Map<String, CryptTask> cryptTasks;

    private transient Map<String, StatusTask> statusCache;

    private transient Map<String, String> properties;

    private CryptTask task;

    CryptDevice() {
        readResolve();
    }

    private Object readResolve() {
        this.statusCache = new HashMap<String, StatusTask>();
        this.properties = new HashMap<String, String>();
        return this;
    }

    @Override
    protected void argumentsSet(Map<String, Object> args,
            List<Object> unnamedArgs) throws Exception {
        super.argumentsSet(args, unnamedArgs);
        mountable.setDevice(this);
        resizeable.setDevice(this);
    }

    @Override
    protected void doCall() throws Exception {
        super.doCall();
        Command cmd = loader.createCommand(BLKID_COMMAND, getTheEnvironment(),
                getArgs(), getOutput(), getError(), getInput(), getThePath());
        getTheEnvironment().executeCommandAndWait(cmd);
        properties.put(TYPE_KEY, (String) getProperty(cmd, THE_TYPE_KEY));
        properties.put(LABEL_KEY, (String) getProperty(cmd, THE_LABEL_KEY));
        properties.put(UUID_KEY, (String) getProperty(cmd, THE_UUID_KEY));
    }

    public Map<String, String> getTheProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Opens the encrypted device under the specified name.
     * 
     * @param name
     *            the name of the opened device.
     * 
     * @throws IOException
     *             if there was an error opening the device.
     */
    @SuppressWarnings("unchecked")
    public void open(String name) throws IOException {
        open(Collections.EMPTY_MAP, name);
    }

    /**
     * Opens the encrypted device under the specified name.
     * 
     * @param args
     *            the pass additional {@link Map} arguments, like the passport
     *            or passphrase.
     * 
     * @param name
     *            the name of the opened device.
     * 
     * @throws IOException
     *             if there was an error opening the device.
     */
    public void open(Map<String, Object> args, String name) throws IOException {
        CryptTask task = getCryptTask();
        task.open(args, name);
        StatusTask statusTask = statusTaskFactory.create(this);
        statusTask.checkDevice(name);
        statusCache.put(name, statusTask);
    }

    /**
     * Checks if the encrypted device is open under the specified name.
     * 
     * @param name
     *            the name of the opened device.
     * 
     * @return {@code true} if the device is open under the specified name.
     * 
     * @throws IOException
     *             if there was an error checking the device.
     */
    public boolean isOpen(String name) throws IOException {
        return isEqualStatus(getStatus(name), true);
    }

    /**
     * Closes the encrypted device under the specified name.
     * 
     * @param name
     *            the name of the opened device.
     * 
     * @throws IOException
     *             if there was an error closing the device.
     */
    public void close(String name) throws IOException {
        CryptTask task = getCryptTask();
        task.close(name);
        statusCache.put(name, null);
    }

    private CryptTask getCryptTask() {
        CryptTask task = this.task;
        if (task == null) {
            task = cryptTasks.get(getTheType());
            if (task == null) {
                throw log.unsupportedFileSystem(this);
            }
            task.setDevice(this);
        }
        this.task = task;
        return task;
    }

    @Override
    public void mount(File path) throws IOException {
        mountable.mount(path);
    }

    @Override
    public void mount(boolean mount, File path) throws IOException {
        mountable.mount(mount, path);
    }

    @Override
    public void umount(File path) throws IOException {
        mountable.umount(path);
    }

    @Override
    public void umount() throws IOException {
        mountable.umount();
    }

    @Override
    public boolean isMounted(File path) throws IOException {
        return mountable.isMounted(path);
    }

    @Override
    public void fsck() throws IOException {
        mountable.fsck();
    }

    @Override
    public void fsck(boolean force) throws IOException {
        mountable.fsck(force);
    }

    @Override
    public void autoFsck() throws IOException {
        mountable.autoFsck();
    }

    @Override
    public void autoFsck(boolean force) throws IOException {
        mountable.autoFsck(force);
    }

    @Override
    public long size(Object unit) throws IOException {
        String name = getTheFirstOpen();
        StatusTask task = getStatus(name);
        log.checkDeviceOpen(this, task.isStatus());
        long size = task.getSize();
        BlockDeviceUnits units = (BlockDeviceUnits) unit;
        switch (units) {
        case SECTORS:
            return size;
        case BYTE_SIZE:
            return size * 512;
        default:
            throw log.unsupportedSize(this, unit);
        }
    }

    @Override
    public void resize(long size, Object unit) throws IOException {
        resizeable.resize(size, unit);
        String name = getTheFirstOpen();
        StatusTask task = createStatusTask(name);
        statusCache.put(name, task);
    }

    @Override
    public String getTheLabel() {
        return properties.get(LABEL_KEY);
    }

    @Override
    public String getTheUUID() {
        return properties.get(UUID_KEY);
    }

    public String getTheType() {
        return properties.get(TYPE_KEY);
    }

    @Override
    public String getTheName() {
        return CryptDeviceService.ID;
    }

    /**
     * Returns the first opened name of the container.
     * 
     * @return the first opened {@link String} name or {@code null}.
     */
    public String getTheFirstOpen() {
        for (Map.Entry<String, StatusTask> entry : statusCache.entrySet()) {
            if (isEqualStatus(entry.getValue(), true)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isEqualStatus(StatusTask status, boolean b) {
        return status != null && status.isStatus() == b;
    }

    private StatusTask getStatus(String name) throws IOException {
        StatusTask task = statusCache.get(name);
        if (task == null) {
            task = createStatusTask(name);
        }
        return task;
    }

    private StatusTask createStatusTask(String name) throws IOException {
        StatusTask statusTask = statusTaskFactory.create(this);
        statusTask.checkDevice(name);
        statusCache.put(name, statusTask);
        return statusTask;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append(LABEL, getTheLabel()).append(UUID, getTheUUID())
                .toString();
    }
}
