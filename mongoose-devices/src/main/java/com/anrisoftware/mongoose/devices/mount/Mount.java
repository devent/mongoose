package com.anrisoftware.mongoose.devices.mount;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.devices.api.Mountable;
import com.google.inject.assistedinject.Assisted;

public class Mount implements Mountable {

	private final MountLogger log;

	private final File devicePath;

	private final FsckTask fsck;

	private final MountTask mount;

	private boolean mounted;

	private File mountPath;

	@Inject
	Mount(MountLogger logger, FsckTaskFactory fsckTaskFactory,
			MountTaskFactory mountTaskFactory, @Assisted File devicePath) {
		this.log = logger;
		this.devicePath = devicePath;
		this.fsck = fsckTaskFactory.create(devicePath);
		this.mount = mountTaskFactory.create(devicePath);
		this.mounted = false;
		this.mountPath = null;
		fsck.setMount(this);
		mount.setMount(this);
	}

	public void setEnvironment(Environment environment) {
		mount.setEnvironment(environment);
		fsck.setEnvironment(environment);
	}

	public void setOutput(Object output) {
		mount.setOutput(output);
		fsck.setOutput(output);
	}

	public void setError(Object error) {
		mount.setError(error);
		fsck.setError(error);
	}

	public void setInput(Object source) {
		mount.setInput(source);
		fsck.setInput(source);
	}

	public File getDevicePath() {
		return devicePath;
	}

	public boolean isMounted() {
		return mounted;
	}

	public File getMountPath() {
		return mountPath;
	}

	@Override
	public void mount(boolean mount, File path) throws IOException {
		log.checkPath(this, mount, path);
		if (mount) {
			// TODO Auto-generated method stub
			mounted = true;
			mountPath = path;
		} else {
			mounted = false;
		}
	}

	@Override
	public void mount(boolean mount) throws IOException {
		mount(mount, null);
		mounted = false;
	}

	@Override
	public boolean isMounted(File path) throws IOException {
		log.checkPath(this, path);
		if (mounted && mountPath.equals(path)) {
			return true;
		} else {
			return mount.checkMounted(path);
		}
	}

	@Override
	public void autoFsck() throws IOException {
		fsck.autoFsck();
	}

	@Override
	public void autoFsck(boolean force) throws IOException {
		fsck.autoFsck(force);
	}

	@Override
	public void fsck() throws IOException {
		fsck.fsck(false);
	}

	@Override
	public void fsck(boolean force) throws IOException {
		fsck.fsck(force);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("device path", devicePath)
				.append("mounted", mounted).append("mount path", mountPath)
				.toString();
	}

}
