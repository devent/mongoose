package com.anrisoftware.mongoose.parser;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BashParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.3
 */
class BashParserLogger extends AbstractLogger {

	/**
	 * Creates logger for {@link BashParser}.
	 */
	BashParserLogger() {
		super(BashParser.class);
	}

	void addClasspathToScript(String path) {
		log.debug("Add the classpath ``{}'' to the script.", path);
	}
}
