package com.anrisoftware.mongoose.devices.blockdevice;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.mongoose.api.commans.CommandFactory;
import com.anrisoftware.mongoose.api.commans.CommandInfo;
import com.anrisoftware.mongoose.api.commans.CommandService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Makes the build-in command {@code block} available as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(CommandService.class)
public class BlockDeviceService implements CommandService {

	/**
	 * The unique identifier of the {@code block} device.
	 */
	public static final String ID = "block";

	/**
	 * The command information of the {@code block} device.
	 */
	public static final CommandInfo INFO = new CommandInfo() {

		@Override
		public String getId() {
			return ID;
		}

	};

	private static final Module[] MODULES = new Module[] { new BlockDeviceModule() };

	@Override
	public CommandInfo getInfo() {
		return INFO;
	}

	@Override
	public CommandFactory getCommandFactory(Object... external) {
		return createInjector(
				(Injector) (external.length > 0 ? external[0] : null))
				.getInstance(CommandFactory.class);
	}

	private Injector createInjector(Injector parent) {
		return parent == null ? Guice.createInjector(MODULES) : parent
				.createChildInjector(MODULES);
	}
}
