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
package com.anrisoftware.mongoose.devices.blkidbuildin
import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.apache.commons.io.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.anrisoftware.mongoose.threads.ThreadsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see BlkidBuildin
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class BlkidTest {

	@Test
	void "disk partition"() {
		command "/dev/sda2"
		println command
	}

	BlkidBuildin command

	Environment environment

	ByteArrayOutputStream byteOutput

	ByteArrayOutputStream byteError

	@Before
	void setupCommand() {
		command = injector.getInstance(BlkidBuildin)
		environment = injector.getInstance(Environment)
		command.setEnvironment environment
		byteOutput = new ByteArrayOutputStream()
		byteError = new ByteArrayOutputStream()
		//command.setOutput(byteOutput)
		//command.setError(byteError)
	}

	@After
	void logErrors() {
		log.info output(byteError)
	}

	File testImage

	String devicePath

	@Before
	void loadTestDevice() {
		testImage = File.createTempFile("test", "dd")
		FileUtils.copyURLToFile deviceImage, testImage
		def out = executeCommand("sudo /sbin/losetup --find --show ${testImage.absolutePath}")
		devicePath = out.out
		log.info "losetup: {}", out.out
		log.error "losetup: {}", out.err
	}

	@After
	void removeMountTestDevice() {
		def out = executeCommand("sudo /sbin/losetup -d $devicePath")
		testImage.delete()
	}

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	static Injector injector


	@BeforeClass
	static void setupInjector() {
		toStringStyle
		injector = Guice.createInjector(
				new BlkidModule(), new EnvironmentModule(), new ThreadsModule(),
				new ResourcesModule())
	}

	static String output(ByteArrayOutputStream stream) {
		stream.toString()
	}
}
