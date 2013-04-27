package com.anrisoftware.mongoose.parser;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ScriptPackageToClasspath}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptPackageToClasspathLogger extends AbstractLogger {

	/**
	 * Create logger for {@link ScriptPackageToClasspath}.
	 */
	public ScriptPackageToClasspathLogger() {
		super(ScriptPackageToClasspath.class);
	}

	void pathAdded(String path) {
		log.debug("Add path '{}' to class path.", path);
	}
}
