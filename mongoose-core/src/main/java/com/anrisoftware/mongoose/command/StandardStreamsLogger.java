package com.anrisoftware.mongoose.command;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link StandardStreams}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StandardStreamsLogger extends AbstractLogger {

	private static final String UNKNOWN_FILE_DESCRIPTOR = "Unknown file descriptor %d";
	private static final String OUTPUT_TARGET_NULL = "Output target cannot be null.";
	private static final String INPUT_SOURCE_NULL = "Input source cannot be null.";

	/**
	 * Create logger for {@link StandardStreams}.
	 */
	public StandardStreamsLogger() {
		super(StandardStreams.class);
	}

	void checkSource(Object obj) {
		notNull(obj, INPUT_SOURCE_NULL);
	}

	void checkTarget(Object obj) {
		notNull(obj, OUTPUT_TARGET_NULL);
	}

	IllegalArgumentException unknownFileDescriptor(int descriptor) {
		String message = format(UNKNOWN_FILE_DESCRIPTOR, descriptor);
		return logException(new IllegalArgumentException(message), message);
	}
}
