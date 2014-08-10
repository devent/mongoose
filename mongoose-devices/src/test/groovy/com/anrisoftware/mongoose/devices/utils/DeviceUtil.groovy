package com.anrisoftware.mongoose.devices.utils

import static org.apache.commons.io.FileUtils.*

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.mongoose.api.commans.Command
import com.anrisoftware.mongoose.api.environment.Environment
import com.google.inject.Injector

/**
 * Setups a test image as block device.
 *
 * For the tests the sudoers needs to be edited:
 * <pre>
 * #Defaults requiretty
 *
 * ## Allows people in group mongoose to run all commands
 * %mongoose ALL=(ALL) NOPASSWD: ALL
 * </pre>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DeviceUtil {

    private final static Logger LOG = LoggerFactory.getLogger(DeviceUtil)

    /**
     * Creates the test image.
     */
    static File createTestImage(File file) {
        copyURLToFile deviceImage, file
        return file
    }

    /**
     * Creates the test block device and returns the device path.
     */
    static String createTestDevice(File file) {
        def out = executeCommand("sudo /sbin/losetup --find --show ${file.absolutePath}")
        def devicePath = StringUtils.substring(out.out, 0, -1)
        LOG.info "losetup: '{}' for file '{}'.", devicePath, file
        logErr out
        return devicePath
    }

    /**
     * Unmount the test block device.
     */
    static void umountTestDevice(File file) {
        def out = executeCommand("sudo /usr/bin/umount ${file.absolutePath}")
        logOut out
        logErr out
    }

    /**
     * Removes the test block device.
     */
    static void removeTestDevice(File file) {
        def out = executeCommand("sudo /sbin/losetup -d ${file.absolutePath}")
        logOut out
        logErr out
    }

    /**
     * Removes unused block devices.
     */
    static void removeUnusedDevice() {
        def out = executeCommand("sudo /sbin/losetup -D")
        logOut out
        logErr out
    }

    /**
     * Log the output.
     */
    private static void logOut(def out) {
        out.out.empty ? null : LOG.info("losetup: {}", out.out)
    }

    /**
     * Log the output as error.
     */
    private static void logErr(def out) {
        out.err.empty ? null : LOG.error("losetup: {}", out.err)
    }

    /**
     * Formatted test image.
     */
    static deviceImage = DeviceUtil.class.getResource("/test.dd")

    /**
     * Executes the given command.
     */
    static def executeCommand(String command) {
        def ex = new DefaultExecutor()
        def out = new ByteArrayOutputStream()
        def err = new ByteArrayOutputStream()
        def streams = new PumpStreamHandler(out, err)
        def cmd = CommandLine.parse(command)
        ex.setStreamHandler streams
        try {
            ex.execute(cmd)
        } finally {
            return [out: out.toString(), err: err.toString()]
        }
    }

    /**
     * Returns the content of the stream.
     */
    static String output(ByteArrayOutputStream stream) {
        stream.toString()
    }

    /**
     * Creates the build-in command.
     */
    static Command createCommand(Injector injector, Class type, Environment environment) {
        Command command = injector.getInstance(type)
        command.setEnvironment environment
        return command
    }

    /**
     * Creates the build-in command environment.
     */
    static Environment createEnvironment(Injector injector) {
        injector.getInstance(Environment)
    }

    static {
        TestUtils.toStringStyle
    }
}
