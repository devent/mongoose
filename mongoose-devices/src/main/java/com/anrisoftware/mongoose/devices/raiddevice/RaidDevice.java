package com.anrisoftware.mongoose.devices.raiddevice;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Device;
import com.anrisoftware.mongoose.devices.api.Resizeable;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;
import com.google.inject.assistedinject.Assisted;

public class RaidDevice extends AbstractDevice implements Resizeable {

    private final CommandLoader loader;

    @Inject
    RaidDevice(CommandLoader loader, @Assisted File path) {
        super(path);
        this.loader = loader;
    }

    @Override
    public <T extends Device> T asType(Class<T> type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTheName() {
        return "raid";
    }

    @Override
    public long size(Object unit) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void resize(long size, Object unit) throws IOException {
        // TODO Auto-generated method stub

    }

}
