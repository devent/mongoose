package com.anrisoftware.mongoose.threads;

import java.util.Properties;

/**
 * Factory to create threads pool based on a properties file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PropertiesThreadsFactory {

	/**
	 * Creates the threads.
	 * 
	 * @return the {@link PropertiesThreads}.
	 */
	PropertiesThreads create();

	/**
	 * Creates the threads.
	 * 
	 * @param properties
	 *            the threads pool {@link Properties}.
	 * 
	 * @param name
	 *            the threads pool {@link String} name.
	 * 
	 * @return the {@link PropertiesThreads}.
	 */
	PropertiesThreads create(Properties properties, String name);
}
