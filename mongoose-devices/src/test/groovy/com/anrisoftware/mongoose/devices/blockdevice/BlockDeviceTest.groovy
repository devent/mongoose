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
import com.anrisoftware.mongoose.devices.utils.DeviceUtil
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
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
        BlockDevice block = createCommand injector, BlockDevice, environment
        block testDevice
        assert block.theUUID == "c298c2a2-50d5-4a79-991b-90ac6d9265b3"
    }

    @Test
    void "block device [size]"() {
        BlockDevice block = createCommand injector, BlockDevice, environment
        block testDevice
        assert block.size(BLOCK_SIZE) == 1024
        assert block.size(BLOCK_COUNT) == 1024
        assert block.size(BYTE_SIZE) == 1024 * 1024
        assert block.size(SECTORS) == 2048
    }

    @Test
    void "block device [resize]"() {
        BlockDevice block = createCommand injector, BlockDevice, environment
        block testDevice
        block.resize 800, BLOCK_COUNT
        assert block.size(BLOCK_COUNT) == 800
        block.resize 1024, BLOCK_COUNT
        assert block.size(BLOCK_COUNT) == 1024
    }

    @Test
    void "block device [mount]"() {
        BlockDevice block = createCommand injector, BlockDevice, environment
        block testDevice
        assert block.isMounted(testMount) == false
        block.mount(testMount)
        assert block.isMounted(testMount) == true
        assert new File(testMount, "lost+found").exists()
        block.umount(testMount)
        assert block.isMounted(testMount) == false
    }

    @Test
    void "block device [fsck]"() {
        BlockDevice block = createCommand injector, BlockDevice, environment
        block testDevice
        block.autoFsck()
    }

    Environment environment

    DeviceUtil device

    File testImage

    File testDevice

    File testMount

    @Before
    void mountTestDevice() {
        environment = createEnvironment(injector)
        testImage = tmp.newFile()
        testMount = tmp.newFolder()
        testImage = createTestImage(testImage)
        testDevice = new File(createTestDevice(testImage))
    }

    @After
    void removeMountTestDevice() {
        umountTestDevice(testDevice)
        removeTestDevice(testDevice)
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    static Injector injector

    @BeforeClass
    static void setupInjector() {
        toStringStyle
        injector = Guice.createInjector(new BlockDeviceModule(),
                new PropertiesThreadsModule(), new EnvironmentModule(), new ResourcesModule())
    }

    @AfterClass
    static void cleanUp() {
        removeUnusedDevice()
    }
}
