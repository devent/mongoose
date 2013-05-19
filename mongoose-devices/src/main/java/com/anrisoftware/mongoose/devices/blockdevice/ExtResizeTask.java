package com.anrisoftware.mongoose.devices.blockdevice;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Resize an ext2/3/4 file system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExtResizeTask implements ResizeTask {

	private static final String SUDO_COMMAND = "sudo";

	private static final String DUMPE2FS_PROPERTY = "dumpe2fs_command";

	private static final String RESIZE2FS_PROPERTY = "resize2fs_command";

	private final ExtResizeTaskLogger log;

	private final CommandLoader loader;

	private final Pattern blockCountPattern;

	private final Pattern blockSizePattern;

	private String resizeCommand;

	private String infoCommand;

	private AbstractDevice device;

	private Long blockSize;

	private Long blockCount;

	@Inject
	ExtResizeTask(ExtResizeTaskLogger logger, CommandLoader loader,
			@Named("blockdevice.properties") ContextProperties p) {
		this.log = logger;
		this.loader = loader;
		this.resizeCommand = p.getProperty(RESIZE2FS_PROPERTY);
		this.infoCommand = p.getProperty(DUMPE2FS_PROPERTY);
		this.blockCountPattern = compile(p.getProperty("block_count_pattern"));
		this.blockSizePattern = compile(p.getProperty("block_size_pattern"));
		this.blockCount = null;
		this.blockSize = null;
	}

	/**
	 * Probes the environment variables:
	 * <dl>
	 * 
	 * <dt>{@link #DUMPE2FS_PROPERTY}</dt>
	 * <dd>the command to probe properties of the file system, {@code dumpe2fs.}
	 * </dd>
	 * 
	 * <dt>{@link #RESIZE2FS_PROPERTY}</dt>
	 * <dd>the command to resize the file system, {@code resize2fs.}</dd>
	 * 
	 * </dl>
	 */
	@Override
	public void setDevice(AbstractDevice device) {
		this.device = device;
		Map<String, String> env = device.getTheEnvironment().getEnv();
		if (env.containsKey(RESIZE2FS_PROPERTY.toUpperCase())) {
			resizeCommand = env.get(RESIZE2FS_PROPERTY.toUpperCase());
		}
		if (env.containsKey(DUMPE2FS_PROPERTY.toUpperCase())) {
			infoCommand = env.get(DUMPE2FS_PROPERTY.toUpperCase());
		}
	}

	@Override
	public long size(Object unit) throws CommandException {
		parseDeviceInfo();
		BlockDeviceUnits u = (BlockDeviceUnits) unit;
		switch (u) {
		case BLOCK_COUNT:
			return blockCount;
		case BLOCK_SIZE:
			return blockSize;
		case BYTE_SIZE:
			return blockCount * blockSize;
		case SECTORS:
			return blockCount * blockSize / 512;
		default:
			throw log.notValidUnit(this, unit);
		}
	}

	private void parseDeviceInfo() throws CommandException {
		if (blockCount == null || blockSize == null) {
			String outstr = getDeviceInfo();
			blockSize = parseBlockSize(outstr);
			blockCount = parseBlockCount(outstr);
		}
	}

	private long parseBlockCount(String outstr) {
		Matcher matcher = blockCountPattern.matcher(outstr);
		log.checkBlockCount(this, matcher.find(), outstr);
		return Long.valueOf(matcher.group(1));
	}

	private long parseBlockSize(String outstr) {
		Matcher matcher = blockSizePattern.matcher(outstr);
		log.checkBlockSize(this, matcher.find(), outstr);
		return Long.valueOf(matcher.group(1));
	}

	private String getDeviceInfo() throws CommandException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Command cmd = loader.createCommand(SUDO_COMMAND,
				device.getTheEnvironment(), device.getArgs(), output,
				device.getError(), device.getInput(),
				format("%s -h %s", infoCommand, device.getThePath()));
		device.getTheEnvironment().executeCommandAndWait(cmd);
		return output.toString();
	}

	@Override
	public void resize(long size, Object unit) throws CommandException {
		String sizestr;
		long newblockcount;
		BlockDeviceUnits u = (BlockDeviceUnits) unit;
		switch (u) {
		case BLOCK_COUNT:
			sizestr = format("%d", size);
			newblockcount = size;
			break;
		case BYTE_SIZE:
			sizestr = format("%dK", size / 1024);
			newblockcount = size / blockSize;
			break;
		case SECTORS:
			sizestr = format("%ds", size);
			newblockcount = size * 512 / blockSize;
			break;
		default:
			throw log.notValidUnit(this, unit);
		}
		Command cmd = loader
				.createCommand(
						SUDO_COMMAND,
						device.getTheEnvironment(),
						device.getArgs(),
						device.getOutput(),
						device.getError(),
						device.getInput(),
						format("%s %s %s", resizeCommand, device.getThePath(),
								sizestr));
		device.getTheEnvironment().executeCommandAndWait(cmd);
		blockCount = newblockcount;
	}

	@Override
	public String toString() {
		return device.toString();
	}
}
