package com.anrisoftware.mongoose.devices.mount;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.mongoose.command.AbstractCommand;
import com.anrisoftware.mongoose.devices.api.Mountable;

/**
 * Mount the device on directories and check the file system of the device.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Mount extends AbstractCommand implements Mountable {

	private static final String PATHS = "paths";

	private final MountLogger log;

	private final FsckTaskFactory fsckFactory;

	private final MountTaskFactory mountFactory;

	private final Map<File, Boolean> mountedPaths;

	private MountTask mount;

	private FsckTask fsck;

	private File path;

	@Inject
	Mount(MountLogger logger, FsckTaskFactory fsckFactory,
			MountTaskFactory mountFactory) {
		this.log = logger;
		this.fsckFactory = fsckFactory;
		this.mountFactory = mountFactory;
		this.mountedPaths = new HashMap<File, Boolean>();
	}

	@Override
	protected void doCall() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		super.argumentsSet(args, unnamedArgs);
		log.checkArgs(this, unnamedArgs.size());
		setPath((File) unnamedArgs.get(0));
		this.fsck = fsckFactory.create(this);
		this.mount = mountFactory.create(this);
	}

	/**
	 * Sets the device path.
	 * 
	 * @param path
	 *            the device {@link File} path.
	 */
	public void setPath(File path) {
		this.path = path;
		log.devicePathSet(this, path);
	}

	public File getThePath() {
		return path;
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
	public String getTheName() {
		return "mount";
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this).appendSuper(super
				.toString());
		if (mountedPaths.size() > 0) {
			builder.append(PATHS, mountedPaths.keySet());
		}
		return builder.toString();
	}

}
