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
package com.anrisoftware.mongoose.devices.cryptdevice

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceUnits.*
import static com.anrisoftware.mongoose.devices.utils.DeviceUtil.*
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
import com.anrisoftware.mongoose.devices.blockdevice.BlockDeviceUnits
import com.anrisoftware.mongoose.devices.utils.DeviceUtil
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see CryptDevice
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class CryptDeviceTest {

    @Test
    void "create crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice
        assert device.theUUID == "6f27eafd-2f2b-4d94-9ec1-067fe10c31e1"
        assert device.theType == "crypto_LUKS"
    }

    //@Test
    void "open from stdin, close crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open "test1"
        assert device.isOpen("test1") == true
        device testDevice close "test1"
        assert device.isOpen("test1") == false
    }

    @Test
    void "open, close crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open passphrase: "po2imueB", "test1"
        assert device.isOpen("test1") == true
        device testDevice close "test1"
        assert device.isOpen("test1") == false
    }

    @Test
    void "mount crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice
        shouldFailWith(CommandException) { device.mount testMount }
    }

    @Test
    void "mount open crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open passphrase: "po2imueB", "test1"
        try {
            device.mount testMount
            assert device.isMounted(testMount) == true
        } finally {
            device.umount testMount
            device testDevice close "test1"
        }
    }

    @Test
    void "auto-fsck open crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open passphrase: "po2imueB", "test1"
        try {
            device.autoFsck()
        } finally {
            device testDevice close "test1"
        }
    }

    @Test
    void "size crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice
        shouldFailWith(CommandException) { device.size BYTE_SIZE }
    }

    @Test
    void "size open crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open passphrase: "po2imueB", "test1"
        try {
            assert device.size(BYTE_SIZE) == 1048576
        } finally {
            device testDevice close "test1"
        }
    }

    @Test
    void "resize open crypt device"() {
        CryptDevice device = createCommand(injector, CryptDevice, environment)
        device testDevice open passphrase: "po2imueB", "test1"
        try {
            device.resize(943718, BYTE_SIZE)
            assert device.size(BYTE_SIZE) == 943616
        } finally {
            device.resize(2048, SECTORS)
            device testDevice close "test1"
        }
    }

    Environment environment

    DeviceUtil device

    File testImage

    File testDevice

    File testMount

    @Before
    void setupTestDevice() {
        environment = createEnvironment(injector)
        testImage = tmp.newFile()
        testMount = tmp.newFolder()
        testImage = createTestImage(testImage, luksCryptDeviceImage)
        testDevice = new File(createTestDevice(testImage))
    }

    @After
    void removeMountTestDevice() {
        umountTestDevice(testMount)
        removeTestDevice(testDevice)
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    static Injector injector

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        injector = Guice.createInjector(new CryptDeviceModule(),
                new PropertiesThreadsModule(), new EnvironmentModule(), new ResourcesModule())
    }

    @AfterClass
    static void cleanUp() {
        removeUnusedDevice()
    }
}
