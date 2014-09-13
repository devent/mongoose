package com.anrisoftware.mongoose.devices.cryptdevice;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Mountable;

/**
 * Manage mountables for opened crypt containers.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MountTask implements Mountable {

    private static final String NAME = "name";

    private static final String MAPPER_DEVICE_TEMPLATE_PROEPRTY = "mapper_device_template";

    private static final String BLOCK_COMMAND = "block";

    private transient Map<String, Mountable> mountableCache;

    @Inject
    private MountTaskLogger log;

    @Inject
    private CryptDevicePropertiesProvider properties;

    @Inject
    private CommandLoader loader;

    private CryptDevice device;

    MountTask() {
        readResolve();
    }

    public void setDevice(CryptDevice device) {
        this.device = device;
    }

    private Object readResolve() {
        this.mountableCache = new HashMap<String, Mountable>();
        return this;
    }

    @Override
    public void mount(boolean mount, File path) throws IOException {
        if (mount) {
            mount(path);
        } else {
            umount(path);
        }
    }

    @Override
    public void mount(File path) throws IOException {
        getMountable().mount(path);
    }

    @Override
    public void umount(File path) throws IOException {
        getMountable().umount(path);
    }

    @Override
    public void umount() throws IOException {
        getMountable().umount();
    }

    @Override
    public boolean isMounted(File path) throws IOException {
        return getMountable().isMounted(path);
    }

    @Override
    public void fsck() throws IOException {
        getMountable().fsck();
    }

    @Override
    public void fsck(boolean force) throws IOException {
        getMountable().fsck(force);
    }

    @Override
    public void autoFsck() throws IOException {
        getMountable().autoFsck();
    }

    @Override
    public void autoFsck(boolean force) throws IOException {
        getMountable().autoFsck(force);
    }

    private Mountable getMountable() throws IOException {
        String name = device.getTheFirstOpen();
        log.checkOpenedName(device, name);
        Mountable mountable = lazyCreateMountable(name,
                mountableCache.get(name));
        return mountable;
    }

    private Mountable lazyCreateMountable(String name, Mountable mountable)
            throws IOException {
        if (mountable != null) {
            return mountable;
        }
        String mapperDevice = new ST(properties.get().getProperty(
                MAPPER_DEVICE_TEMPLATE_PROEPRTY)).add(NAME, name).render();
        mountable = (Mountable) loader.createCommand(BLOCK_COMMAND,
                device.getTheEnvironment(), device.getArgs(),
                device.getOutput(), device.getError(), device.getInput(),
                new File(mapperDevice));
        mountableCache.put(name, mountable);
        return mountable;
    }

}
