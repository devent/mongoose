package com.anrisoftware.mongoose.devices.blockdevice;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.mongoose.devices.api.Block;
import com.anrisoftware.mongoose.devices.device.AbstractDevice;

/**
 * Block device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class BlockDevice extends AbstractDevice implements Block {

	private static final String BLKID_COMMAND = "blkid";

	private final CommandLoader loader;

	private final Map<String, ResizeTask> resizeTasks;

	private String name;

	private String label;

	private String uuid;

	private String id;

	private String type;

	private ResizeTask task;

	@Inject
	BlockDevice(CommandLoader loader, Map<String, ResizeTask> resizeTasks) {
		this.loader = loader;
		this.resizeTasks = resizeTasks;
	}

	@Override
	protected void doCall() throws Exception {
		super.doCall();
		Command cmd = loader.createCommand(BLKID_COMMAND, getTheEnvironment(),
				getArgs(), getOutput(), getError(), getInput(), getThePath());
		getTheEnvironment().executeCommandAndWait(cmd);
		this.type = (String) InvokerHelper.getProperty(cmd, "theType");
		this.label = (String) InvokerHelper.getProperty(cmd, "theLabel");
		this.uuid = (String) InvokerHelper.getProperty(cmd, "theUUID");
	}

	@Override
	public void mount(File path) throws IOException {
		mount.mount(path);
	}

	@Override
	public void mount(boolean mount, File path) throws IOException {
		this.mount.mount(mount, path);
	}

	@Override
	public void umount(File path) throws IOException {
		mount.umount(path);
	}

	@Override
	public void umount() throws IOException {
		mount.umount();
	}

	@Override
	public boolean isMounted(File path) throws IOException {
		return mount.isMounted(path);
	}

	@Override
	public void fsck() throws IOException {
		mount.fsck();
	}

	@Override
	public void fsck(boolean force) throws IOException {
		mount.fsck(force);
	}

	@Override
	public void autoFsck() throws IOException {
		mount.autoFsck();
	}

	@Override
	public void autoFsck(boolean force) throws IOException {
		mount.autoFsck(force);
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
		return name;
	}

}
