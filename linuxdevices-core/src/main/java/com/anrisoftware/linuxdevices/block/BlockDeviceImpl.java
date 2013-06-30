package com.anrisoftware.linuxdevices.block;

import static com.anrisoftware.linuxdevices.block.BlockModule.LINUX_DEVICES_CONTEXT;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;

import com.anrisoftware.linuxdevices.command.api.CommandWorker;
import com.anrisoftware.linuxdevices.command.api.ReadStandardStreams;
import com.anrisoftware.linuxdevices.command.factories.ReadStandardStreamsFactory;
import com.anrisoftware.linuxdevices.command.factories.SuCommandWorkerFactory;
import com.anrisoftware.linuxdevices.core.api.Block;
import com.anrisoftware.linuxdevices.core.api.BlockUnit;
import com.anrisoftware.mongoose.devices.core.AbstractDevice;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Implement checking, re-sizing and mounting of the block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class BlockDeviceImpl extends AbstractDevice implements Block {

	private final BlockDeviceImplLogger log;

	private final SuCommandWorkerFactory commandFactory;

	private final Map<File, File> mountPaths;

	private final ContextProperties properties;

	private final ReadStandardStreamsFactory streamsFactory;

	@AssistedInject
	BlockDeviceImpl(BlockDeviceImplLogger logger,
			@Named("block-device-properties") Properties properties,
			ReadStandardStreamsFactory streamsFactory,
			SuCommandWorkerFactory commandFactory, @Assisted String name,
			@Assisted File path) {
		super(name, path);
		this.log = logger;
		this.properties = new ContextProperties(LINUX_DEVICES_CONTEXT,
				properties);
		this.commandFactory = commandFactory;
		this.mountPaths = new HashMap<File, File>();
		this.streamsFactory = streamsFactory;
	}

	@AssistedInject
	BlockDeviceImpl(BlockDeviceImplLogger logger,
			@Named("block-device-properties") Properties properties,
			ReadStandardStreamsFactory streamsFactory,
			SuCommandWorkerFactory commandFactory, @Assisted File path) {
		super(path);
		this.log = logger;
		this.properties = new ContextProperties(LINUX_DEVICES_CONTEXT,
				properties);
		this.streamsFactory = streamsFactory;
		this.commandFactory = commandFactory;
		this.mountPaths = new HashMap<File, File>();
	}

	@Override
	public void setMounted(boolean mount, File path) throws IOException {
		log.checkMountPath(this, path);
		if (mount) {
			mountDevice(path);
		} else {
			umountDevice(path);
		}
	}

	private void mountDevice(File path) throws IOException {
		if (mountPaths.containsKey(path)) {
			return;
		}
		if (doMount(path)) {
			mountPaths.put(path, path);
		}
	}

	private boolean doMount(File path) throws IOException {
		String command = getMountCommand(path);
		ReadStandardStreams streams = streamsFactory.create();
		CommandWorker worker = commandFactory.create(command, streams).call();
		int code = worker.getExitCode();
		if (code == 0) {
			return true;
		} else {
			throw log.errorMountDevice(this, path, code, streams.readError());
		}
	}

	private String getMountCommand(File path) {
		String command = properties
				.getProperty(BlockModule.MOUNT_COMMAND_PROPERTY);
		return command.replace("{device}", getPath().getAbsolutePath())
				.replace("{dir}", path.getAbsolutePath());
	}

	private void umountDevice(File path) {
		if (!mountPaths.containsKey(path)) {
			return;
		}

	}

	@Override
	public boolean isMounted(File path) throws IOException {
		log.checkMountPath(this, path);
		if (mountPaths.containsKey(path)) {
			return true;
		}
		if (doCheckMount(path)) {
			mountPaths.put(path, path);
			return true;
		} else {
			return false;
		}
	}

	private boolean doCheckMount(File path) throws IOException {
		String command = getCheckMountCommand(path);
		ReadStandardStreams streams = streamsFactory.create();
		CommandWorker worker = commandFactory.create(command, streams).call();
		int code = worker.getExitCode();
		if (code == 0) {
			return true;
		} else {
			String error = streams.readError();
			if (!isEmpty(error)) {
				throw log.errorCheckMounted(this, path, code, error);
			} else {
				return false;
			}
		}
	}

	private String getCheckMountCommand(File path) {
		String command = properties
				.getProperty(BlockModule.CHECK_MOUNT_COMMAND_PROPERTY);
		return command.replace("{device}", getPath().getAbsolutePath())
				.replace("{dir}", path.getAbsolutePath());
	}

	@Override
	public void checkFilesystem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkFilesystem(boolean forceIfMounted) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getBlockSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getByteSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resize(long newSize, BlockUnit unit) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUuid() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getUnmountCommand() {
		return properties.getProperty("unmount_command");
	}
}
