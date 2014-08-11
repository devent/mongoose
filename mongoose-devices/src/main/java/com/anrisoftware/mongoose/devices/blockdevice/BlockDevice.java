package com.anrisoftware.mongoose.devices.blockdevice;

import static org.codehaus.groovy.runtime.InvokerHelper.getProperty;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Block;
import com.anrisoftware.mongoose.devices.api.Mountable;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;

/**
 * Block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BlockDevice extends AbstractDevice implements Block {

    private static final String UUID = "uuid";

    private static final String LABEL = "label";

    private static final String ID = "id";

    private static final String THE_UUID_KEY = "theUUID";

    private static final String THE_LABEL_KEY = "theLabel";

    private static final String THE_TYPE_KEY = "theType";

    private static final String MOUNT_COMMAND = "mount";

    private static final String BLKID_COMMAND = "blkid";

    private final CommandLoader loader;

    private final Map<String, ResizeTask> resizeTasks;

    private String label;

    private String uuid;

    private String id;

    private String type;

    private ResizeTask task;

    private Command mount;

    private Mountable mountable;

    @Inject
    BlockDevice(CommandLoader loader, Map<String, ResizeTask> resizeTasks) {
        this.loader = loader;
        this.resizeTasks = resizeTasks;
    }

    @Override
    protected void argumentsSet(Map<String, Object> args,
            List<Object> unnamedArgs) throws Exception {
        super.argumentsSet(args, unnamedArgs);
        mount = loader.createCommand(MOUNT_COMMAND, getTheEnvironment(),
                getArgs(), getOutput(), getError(), getInput(), getThePath());
        mountable = (Mountable) mount;
    }

    @Override
    protected void doCall() throws Exception {
        super.doCall();
        Command cmd = loader.createCommand(BLKID_COMMAND, getTheEnvironment(),
                getArgs(), getOutput(), getError(), getInput(), getThePath());
        getTheEnvironment().executeCommandAndWait(cmd);
        this.type = (String) getProperty(cmd, THE_TYPE_KEY);
        this.label = (String) getProperty(cmd, THE_LABEL_KEY);
        this.uuid = (String) getProperty(cmd, THE_UUID_KEY);
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
    public long size(Object unit) throws CommandException {
        return getTask().size(unit);
    }

    @Override
    public void resize(long size, Object unit) throws IOException {
        getTask().resize(size, unit);
    }

    private ResizeTask getTask() {
        if (task == null) {
            task = resizeTasks.get(type);
            task.setDevice(this);
        }
        return task;
    }

    @Override
    public String getTheId() {
        return id;
    }

    @Override
    public String getTheLabel() {
        return label;
    }

    @Override
    public String getTheUUID() {
        return uuid;
    }

    @Override
    public String getTheName() {
        return BlockDeviceService.ID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append(ID, id).append(LABEL, label).append(UUID, uuid)
                .toString();
    }
}
