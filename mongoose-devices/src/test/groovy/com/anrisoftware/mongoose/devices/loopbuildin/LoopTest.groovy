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
package com.anrisoftware.mongoose.devices.loopbuildin
import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.devices.utils.DeviceUtil.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.api.exceptions.CommandException
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see LoopBuildin
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LoopTest {

    @Test
    void "with image"() {
        def testImage = createTestImage(tmp.newFile())
        command testImage
        assert command.theDevice.exists() == true
        removeTestDevice(command.theDevice)
    }

    @Test
    void "with image, with terminal"() {
        def testImage = createTestImage(tmp.newFile())
        try {
            command "terminal": true, testImage
            assert command.theDevice.exists() == true
        } finally {
            removeTestDevice(command.theDevice)
        }
    }

    @Test(expected = CommandException)
    void "no image"() {
        command "xxx"
    }

    @Test
    void "with image, delete device"() {
        def testImage = createTestImage(tmp.newFile())
        command testImage
        command.delete()
        assert command.theDevice == null
    }

    @Test
    void "with image, already device"() {
        def testImage = createTestImage(tmp.newFile())
        def devicePath = createTestDevice(testImage)
        try {
            command testImage, devicePath
            assert command.theDevice.absolutePath == devicePath
        } finally {
            removeTestDevice(devicePath as File)
        }
    }

    @Test
    void "with image, custom device, setup"() {
        def testImage = createTestImage(tmp.newFile())
        try {
            command testImage, "/dev/loop9"
            assert command.theDevice.exists() == true
        } finally {
            removeTestDevice(command.theDevice)
        }
    }

    @Test
    void "with image, custom device name, setup"() {
        def testImage = createTestImage(tmp.newFile())
        try {
            command testImage, "loop9"
            assert command.theDevice.exists() == true
        } finally {
            removeTestDevice(command.theDevice)
        }
    }

    @Test
    void "with image, already +device, delete"() {
        def testImage = createTestImage(tmp.newFile())
        def devicePath = createTestDevice(testImage)
        try {
            command testImage, devicePath
            command.delete()
            assert command.theDevice == null
        } finally {
            if (command.theDevice) {
                removeTestDevice(command.theDevice)
            }
        }
    }

    LoopBuildin command

    Environment environment

    ByteArrayOutputStream byteOutput

    ByteArrayOutputStream byteError

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    static Injector injector

    @Before
    void setupCommand() {
        command = injector.getInstance(LoopBuildin)
        environment = injector.getInstance(Environment)
        command.setEnvironment environment
        byteOutput = new ByteArrayOutputStream()
        byteError = new ByteArrayOutputStream()
        command.setOutput(byteOutput)
        command.setError(byteError)
    }

    @After
    void logErrors() {
        def str = output byteError
        str.empty ? null : log.error("{}", byteError)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        injector = Guice.createInjector(new LoopModule(),
                new PropertiesThreadsModule(), new EnvironmentModule(), new ResourcesModule())
    }

    @AfterClass
    static void removeDevices() {
        removeUnusedDevice()
    }

    static String output(ByteArrayOutputStream stream) {
        stream.toString()
    }
}
