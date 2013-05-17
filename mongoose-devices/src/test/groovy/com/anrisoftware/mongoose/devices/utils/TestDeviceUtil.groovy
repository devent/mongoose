package com.anrisoftware.mongoose.devices.utils;

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.lang3.StringUtils

/**
 * Setups a test image as block device
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class TestDeviceUtil {

	File testImage

	String devicePath

	void createTestDevice() {
		def out = executeCommand("sudo /sbin/losetup --find --show ${testImage.absolutePath}")
		devicePath = StringUtils.substring(out.out, 0, -1)
		log.info "losetup: {}", devicePath
		log.error "losetup: {}", out.err
	}

	void createTestImage() {
		testImage = File.createTempFile("test", "dd")
		copyURLToFile deviceImage, testImage
	}

	void removeTestDevice() {
		def out = executeCommand("sudo /sbin/losetup -d $devicePath")
		log.info "losetup: {}", out.out
		log.error "losetup: {}", out.err
	}

	void removeUnusedDevice() {
		def out = executeCommand("sudo /sbin/losetup -D")
		log.info "losetup: {}", out.out
		log.error "losetup: {}", out.err
	}

	void removeTestImage() {
		testImage.delete()
	}

	static deviceImage = TestDeviceUtil.class.getResource("/test.dd")

	static def executeCommand(String command) {
		def ex = new DefaultExecutor()
		def out = new ByteArrayOutputStream();
		def err = new ByteArrayOutputStream();
		def streams = new PumpStreamHandler(out, err)
		def cmd = CommandLine.parse(command)
		ex.setStreamHandler streams
		ex.execute(cmd)
		[out: out.toString(), err: err.toString()]
	}
}