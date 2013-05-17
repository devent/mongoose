package com.anrisoftware.mongoose.devices.mount;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.devices.api.Mountable;
import com.google.inject.assistedinject.Assisted;

/**
 * Mount the device on directories and check the file system of the device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Mount implements Mountable {

	private static final String PATHS = "paths";

	private static final String DEVICE = "device";

	private final MountLogger log;

	private final File device;

	private final FsckTask fsck;

	private final MountTask mount;

	private final Map<File, Boolean> mountedPaths;

	@Inject
	Mount(MountLogger logger, FsckTaskFactory fsckTaskFactory,
			MountTaskFactory mountTaskFactory, @Assisted File device) {
		this.log = logger;
		this.device = device;
		this.fsck = fsckTaskFactory.create(device);
		this.mount = mountTaskFactory.create(device);
		this.mountedPaths = new HashMap<File, Boolean>();
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

	public void setNamed(Map<String, Object> named) {
		mount.setNamed(named);
	}

	public File getDevice() {
		return device;
	}

	@Override
	public void mount(File path) throws IOException {
		mount(true, path);
	}

	@Override
	public void mount(boolean mount, File path) throws IOException {
		if (mount) {
			log.checkPath(this, path);
			log.checkNotMounted(this, mountedPaths.containsKey(path), path);
			log.mountDevice(this, path);
			this.mount.mountDevice(path);
			mountedPaths.put(path, true);
		} else {
			log.checkMounted(this, mountedPaths.containsKey(path), path);
			this.mount.umountDevice(path);
			mountedPaths.put(path, false);
		}
	}

	@Override
	public void umount(File path) throws IOException {
		mount(false, path);
	}

	@Override
	public void umount() throws IOException {
		for (File path : mountedPaths.keySet()) {
			mount(false, path);
		}
	}

	@Override
	public boolean isMounted(File path) throws IOException {
		Boolean mounted = mountedPaths.get(path);
		if (mounted == null) {
			mounted = mount.checkMounted(path);
			mountedPaths.put(path, mounted);
		}
		return mounted;
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
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append(DEVICE, device);
		if (mountedPaths.size() > 0) {
			builder.append(PATHS, mountedPaths.keySet());
		}
		return builder.toString();
	}
}
