package com.anrisoftware.mongoose.devices.device;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Device;
import com.anrisoftware.mongoose.devices.api.DevicesMapper;

public class DeviceMapperImpl implements DevicesMapper {

	private final CommandLoader loader;

	@Inject
	DeviceMapperImpl(CommandLoader loader) {
		this.loader = loader;
	}

	@Override
	public Device getDevice(File path) {
		// TODO Auto-generated method stub
		return null;
	}

}
