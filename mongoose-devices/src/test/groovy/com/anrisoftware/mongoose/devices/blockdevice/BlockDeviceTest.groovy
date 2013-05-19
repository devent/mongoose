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
package com.anrisoftware.mongoose.devices.blockdevice

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceUnits.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.devices.utils.TestDeviceUtil
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see BlockDevice
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class BlockDeviceTest {

	@Test
	void "block device [properties]"() {
		BlockDevice block = injector.getInstance BlockDevice
		block.setEnvironment environment
		block device.devicePath as File
		assert block.theUUID == "c298c2a2-50d5-4a79-991b-90ac6d9265b3"
	}

	@Test
	void "block device [size]"() {
		BlockDevice block = injector.getInstance BlockDevice
		block.setEnvironment environment
		block device.devicePath as File
		assert block.size(BLOCK_SIZE) == 1024
		assert block.size(BLOCK_COUNT) == 1024
		assert block.size(BYTE_SIZE) == 1024 * 1024
		assert block.size(SECTORS) == 2048
	}

	@Test
	void "block device [resize]"() {
		BlockDevice block = injector.getInstance BlockDevice
		block.setEnvironment environment
		block device.devicePath as File
		block.resize 800, BLOCK_COUNT
		assert block.size(BLOCK_COUNT) == 800
		block.resize 1024, BLOCK_COUNT
		assert block.size(BLOCK_COUNT) == 1024
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
		injector = Guice.createInjector(new BlockDeviceModule(),
				new EnvironmentModule(), new ThreadsModule(), new ResourcesModule())
	}
}
