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
package com.anrisoftware.mongoose.devices.devicebuildin
import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.devices.blockdevice.BlockDevice
import com.anrisoftware.mongoose.devices.utils.TestDeviceUtil
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see DeviceBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class DeviceTest {

	@Test
	void "block device, file"() {
		device.createTestDevice()
		command "type": "block", device.devicePath
		assert command.theDevice.class == BlockDevice
	}

	@Test
	void "block device, loop image file"() {
		command "type": "block", device.testImage
		assert command.theDevice.class == BlockDevice
	}

	@Test
	void "block device, loop image file, loop device"() {
		command "type": "block", "loop": "loop5", device.testImage
		assert command.theDevice.class == BlockDevice
	}

	DeviceBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	ByteArrayOutputStream byteError

	@Before
	void setupCommand() {
		command = injector.getInstance(DeviceBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
		byteOutput = new ByteArrayOutputStream()
		byteError = new ByteArrayOutputStream()
		command.setOutput(byteOutput)
		command.setError(byteError)
	}

	@After
	void logErrors() {
		def err = output(byteError)
		if (!err.empty) {
			log.error err
		}
	}

	TestDeviceUtil device

	@Before
	void loadTestDevice() {
		device = new TestDeviceUtil()
		device.createTestImage()
	}

	@After
	void removeMountTestDevice() {
		device.removeTestImage()
		device.removeUnusedDevice()
	}

	static Injector injector

	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(new DeviceModule(),
				new EnvironmentModule(), new ThreadsModule(), new ResourcesModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
