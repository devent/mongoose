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
import com.anrisoftware.mongoose.environment.EnvironmentModule
import com.anrisoftware.mongoose.resources.ResourcesModule
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
    void "image"() {
        command testDevice
        assert command.getTheUUID() == "c298c2a2-50d5-4a79-991b-90ac6d9265b3"
        assert command.getTheType() == "ext2"
    }

    BlkidBuildin command

    Environment environment

    ByteArrayOutputStream byteOutput

    ByteArrayOutputStream byteError

    File testImage

    File testDevice

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    static Injector injector

    @Before
    void setupCommand() {
        environment = createEnvironment(injector)
        byteOutput = new ByteArrayOutputStream()
        byteError = new ByteArrayOutputStream()
        command = createCommand(injector, BlkidBuildin, environment)
        command.setOutput(byteOutput)
        command.setError(byteError)
    }

    @After
    void logErrors() {
        def str = output byteError
        str.empty ? null : log.error("{}", byteError)
    }

    @Before
    void loadTestDevice() {
        testImage = createTestImage(tmp.newFile())
        testDevice = new File(createTestDevice(testImage))
    }

    @After
    void removeMountTestDevice() {
        removeTestDevice(testDevice)
    }

    @BeforeClass
    static void setupInjector() {
        injector = Guice.createInjector(new BlkidModule(),
                new PropertiesThreadsModule(), new EnvironmentModule(), new ResourcesModule())
    }

    @AfterClass
    static void removeUnused() {
        removeUnusedDevice()
    }
}
