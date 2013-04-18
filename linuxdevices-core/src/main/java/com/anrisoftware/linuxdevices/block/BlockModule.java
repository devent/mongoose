package com.anrisoftware.linuxdevices.block;

import static java.lang.System.getProperties;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.linuxdevices.command.factories.BlockDeviceFactory;
import com.anrisoftware.linuxdevices.core.api.Block;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class BlockModule extends AbstractModule {

	public static final String LINUX_DEVICES_CONTEXT = "com.anrisoftware.linuxdevices";

	private static final URL LINUX_DEVICE_PROPERTIES = BlockModule.class
			.getResource("/linux_device.properties");

	static final String MOUNT_COMMAND_PROPERTY = "mount_command";

	static final String CHECK_MOUNT_COMMAND_PROPERTY = "check_mount_command";

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Block.class,
				BlockDeviceImpl.class).build(BlockDeviceFactory.class));
	}

	@Provides
	@Singleton
	@Named("block-device-properties")
	Properties getBlockDeviceProperties() throws IOException {
		return new ContextPropertiesFactory(LINUX_DEVICES_CONTEXT)
				.withProperties(getProperties()).fromResource(
						LINUX_DEVICE_PROPERTIES);
	}
}
