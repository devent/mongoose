package com.anrisoftware.mongoose.devices.blockdevice;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ExtResizeTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ExtResizeTaskLogger extends AbstractLogger {

	private static final String UNIT = "The unit %s is not valid for %s";
	private static final String BLOCK_COUNT = "Could not parse the block count for %s in output ``%s''";
	private static final String BLOCK_SIZE = "Could not parse the block size for %s in output ``%s''";

	/**
	 * Create logger for {@link ExtResizeTask}.
	 */
	public ExtResizeTaskLogger() {
		super(ExtResizeTask.class);
	}

	void checkBlockCount(ExtResizeTask device, boolean find, String outstr) {
		isTrue(find, BLOCK_COUNT, device, outstr);
	}

	void checkBlockSize(ExtResizeTask device, boolean find, String outstr) {
		isTrue(find, BLOCK_SIZE, device, outstr);
	}

	IllegalArgumentException notValidUnit(ExtResizeTask device, Object unit) {
		return logException(
				new IllegalArgumentException(format(UNIT, unit, device)), UNIT,
				unit, device);
	}
}
