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
import static com.anrisoftware.mongoose.devices.utils.DeviceUtil.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.mongoose.api.environment.Environment
import com.anrisoftware.mongoose.devices.blockdevice.BlockDevice
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
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
    void "block device, from lo-device"() {
        def testImage = createTestImage(tmp.newFile())
        def testDevice = new File(createTestDevice(testImage))
        try {
            command type: "block", testDevice
            assert command.theDevice.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from lo-device, direct"() {
        def testImage = createTestImage(tmp.newFile())
        def testDevice = new File(createTestDevice(testImage))
        try {
            def dev = command type: "block", testDevice theDevice
            assert dev.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from lo-device, direct auto-fsck"() {
        def testImage = createTestImage(tmp.newFile())
        def testDevice = new File(createTestDevice(testImage))
        try {
            command type: "block", testDevice autoFsck()
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from lo-device, uri syntax"() {
        def testImage = createTestImage(tmp.newFile())
        def testDevice = new File(createTestDevice(testImage))
        try {
            command "block://${testDevice}"
            assert command.theDevice.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from lo-device, uri syntax, direct"() {
        def testImage = createTestImage(tmp.newFile())
        def testDevice = new File(createTestDevice(testImage))
        try {
            def dev = command "block://${testDevice}" theDevice
            assert dev.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from image file"() {
        def testImage = createTestImage(tmp.newFile())
        try {
            command type: "block", testImage
            assert command.theDevice.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    @Test
    void "block device, from image file, set loop device"() {
        def testImage = createTestImage(tmp.newFile())
        try {
            command type: "block", loop: "loop5", testImage
            assert command.theDevice.class == BlockDevice
        } finally {
            removeUnusedDevice()
        }
    }

    DeviceBuildin command

    Environment environment

    ByteArrayOutputStream byteOutput

    ByteArrayOutputStream byteError

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    static Injector injector

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
        def str = output byteError
        str.empty ? null : log.error("{}", byteError)
    }

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        injector = Guice.createInjector(new DeviceModule(),
                new PropertiesThreadsModule(), new EnvironmentModule(), new ResourcesModule())
    }

    static String output(ByteArrayOutputStream stream) {
        stream.toString()
    }
}
