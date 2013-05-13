package com.anrisoftware.mongoose.devices.blkidbuildin

import java.util.regex.Pattern

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.propertiesutils.ContextProperties
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see BlkidParser
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BlkidParserTest {

	@Test
	void "parse pattern"() {
		inputs.each {
			def pattern = compilePattern it.device
			def matcher = pattern.matcher it.output
			while (matcher.find()) {
				(0..matcher.groupCount()).each {
					println matcher.group(it)
				}
			}
		}
	}

	@Test
	void "parse"() {
		inputs.each {
			def values = [:]
			parser.withDevice it.device withString it.output withValues values build()
			values.SEC_TYPE == it.sec_type
			values.LABEL == it.label
			values.UUID == it.uuid
			values.TYPE == it.type
		}
	}

	static inputs = [
		[
			output: '/dev/sda1: SEC_TYPE="msdos" LABEL="DellUtility" UUID="5450-4444" TYPE="vfat"',
			device: '/dev/sda1',
			label: 'DellUtility',
			uuid: '5450-4444',
			type: 'vfat',
			sec_type: 'msdos'
		],
		[
			output: '/dev/md127: UUID="Pd2HuO-JWnN-EnT2-11Wm-joFf-fEBG-a191qS" TYPE="LVM2_member"',
			device: '/dev/md127',
			label: null,
			uuid: 'Pd2HuO-JWnN-EnT2-11Wm-joFf-fEBG-a191qS',
			type: 'LVM2_member',
			sec_type: null
		],
		[
			output: '/dev/sda3: LABEL="OS" UUID="567E364B7E3623E1" TYPE="ntfs"',
			device: '/dev/sda3',
			label: 'OS',
			uuid: '567E364B7E3623E1',
			type: 'ntfs',
			sec_type: null
		]
	]

	static Injector injector

	static BlkidParser parser

	static ContextProperties properties

	@BeforeClass
	static void loadBlkidProperties() {
		injector = Guice.createInjector(new BlkidModule())
		parser = injector.getInstance BlkidParser
		properties = new BlkidModule().blkidProperties
	}

	Pattern compilePattern(String device) {
		Pattern.compile String.format(properties.getProperty("blkid_pattern"), device)
	}
}
