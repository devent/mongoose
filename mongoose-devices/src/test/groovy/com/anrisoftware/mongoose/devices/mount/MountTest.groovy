/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.devices.mount

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.anrisoftware.mongoose.devices.utils.TestDeviceUtil
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see Mount
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MountTest {

	@Test
	void "mount [check mounted]"() {
		def tmpdir = tmp.newFolder()
		Mount mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File
		mount.isMounted(tmpdir) == false
	}

	@Test
	void "mount [check mounted, +unmount]"() {
		def tmpdir = tmp.newFolder()
		Mount mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File, tmpdir
		mount.isMounted(tmpdir) == true
		mount.umount tmpdir
		assert mount.isMounted(tmpdir) == false
	}

	@Test
	void "mount [check mounted, +mount, different mount]"() {
		def tmpdir = tmp.newFolder()
		Mount mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File, tmpdir

		mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File
		assert mount.isMounted(tmpdir) == true
		mount.umount tmpdir
		assert mount.isMounted(tmpdir) == false
	}

	@Test
	void "mount [check mounted, +mount, different mount, already mounted]"() {
		def tmpdir = tmp.newFolder()
		Mount mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File, tmpdir

		Mount mountb = injector.getInstance Mount
		mountb.setEnvironment environment

		shouldFailWith(CommandException) {
			mountb device.devicePath as File, tmpdir
		}

		mount.umount()
	}

	@Test
	void "auto fsck"() {
		def tmpdir = tmp.newFolder()
		Mount mount = injector.getInstance Mount
		mount.setEnvironment environment
		mount device.devicePath as File
		mount.autoFsck()
	}

	Environment environment

	TestDeviceUtil device

	@Before
	void setupEnvironment() {
		environment = injector.getInstance(Environment)
	}

	@Before
	void mountTestDevice() {
		device = new TestDeviceUtil()
		device.createTestImage()
		device.createTestDevice()
	}

	@After
	void removeMountTestDevice() {
		device.removeTestImage()
		device.removeTestDevice()
	}

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(
				new MountModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}
}
