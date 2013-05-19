package com.anrisoftware.mongoose.devices.blockdevice;

import java.io.IOException;
import java.util.List;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.mongoose.devices.api.DeviceFactory;
import com.anrisoftware.mongoose.devices.api.DeviceService;
import com.anrisoftware.mongoose.devices.api.DeviceServiceInfo;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

@ProviderFor(DeviceService.class)
public class BlockDeviceService implements DeviceService {

	private static final String NOT_FILESYSTEMS_PROPERTY = "not_filesystems";

	private static final String BLOCKDEVICE_PROPERTIES = "blockdevice.properties";

	static {
		NOT_FILESYSTEM = loadFileSystems();
	}

	private static List<String> loadFileSystems() {
		try {
			ContextProperties p = new ContextPropertiesFactory(
					BlockDeviceService.class)
					.fromResource(BlockDeviceService.class
							.getResource(BLOCKDEVICE_PROPERTIES));
			return p.getListProperty(NOT_FILESYSTEMS_PROPERTY);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final List<String> NOT_FILESYSTEM;

	private static final Module[] MODULES = new Module[] { new BlockDeviceModule() };

	public static DeviceServiceInfo INFO = new DeviceServiceInfo() {

		@Override
		protected boolean isEquals(DeviceServiceInfo rhs) {
			return !NOT_FILESYSTEM.contains(rhs.getType());
		}
	};

	@Override
	public DeviceServiceInfo getInfo() {
		return INFO;
	}

	@Override
	public DeviceFactory createDevice(Object... external) {
		return createInjector(
				(Injector) (external.length > 0 ? external[0] : null))
				.getInstance(DeviceFactory.class);
	}

	private Injector createInjector(Injector parent) {
		return parent == null ? Guice.createInjector(MODULES) : parent
				.createChildInjector(MODULES);
	}

}
