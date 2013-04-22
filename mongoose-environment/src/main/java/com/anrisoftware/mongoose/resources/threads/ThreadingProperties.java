package com.anrisoftware.mongoose.resources.threads;

import static java.lang.String.format;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Loads threading properties from a properties file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class ThreadingProperties extends Properties {

	private static final String POOL_FACTORY_KEY = "pool_factory";

	private static final String POLICY_KEY = "policy";

	static final String KEY_TEMPLATE = "%s.%s";

	private ThreadingPropertiesLogger log;

	private ContextProperties properties;

	private String name;

	@Inject
	void setThreadingPropertiesLogger(ThreadingPropertiesLogger log) {
		this.log = log;
	}

	@Assisted
	void setName(String name) {
		this.name = name;
	}

	@Assisted
	void setProperties(ContextProperties properties) {
		this.properties = properties;
	}

	protected ContextProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the threading policy.
	 * 
	 * @return the {@link ThreadingPolicy}.
	 * 
	 * @throws NullPointerException
	 *             if no threading policy was found.
	 */
	public ThreadingPolicy getPolicy() {
		ThreadingPolicy value = getPolicy(null);
		log.checkPolicy(this, value);
		return value;
	}

	/**
	 * Returns the threading policy.
	 * 
	 * @param defaultValue
	 *            the default {@link ThreadingPolicy}.
	 * 
	 * @return the {@link ThreadingPolicy} or the default value.
	 */
	public ThreadingPolicy getPolicy(ThreadingPolicy defaultValue) {
		String value = properties.getProperty(format(KEY_TEMPLATE, name,
				POLICY_KEY));
		return StringUtils.isEmpty(value) ? defaultValue : ThreadingPolicy
				.valueOf(value);
	}

	/**
	 * Returns the thread factory.
	 * 
	 * @return the {@link ThreadFactory}.
	 * 
	 * @throws ThreadsException
	 *             if the thread factory could not be created from the property.
	 * 
	 * @throws NullPointerException
	 *             if no thread factory was found.
	 */
	public ThreadFactory getThreadFactory() throws ThreadsException {
		ThreadFactory value = getThreadFactory(null);
		log.checkThreadFactory(this, value);
		return value;
	}

	/**
	 * Returns the thread factory.
	 * 
	 * @param defaultValue
	 *            the default {@link ThreadFactory}.
	 * 
	 * @return the {@link ThreadFactory} or the default value.
	 * 
	 * @throws ThreadsException
	 *             if the thread factory could not be created from the property.
	 */
	public ThreadFactory getThreadFactory(ThreadFactory defaultValue)
			throws ThreadsException {
		String value = properties.getProperty(format(KEY_TEMPLATE, name,
				POOL_FACTORY_KEY));
		return StringUtils.isEmpty(value) ? defaultValue
				: createFactory(getFactoryType(value));
	}

	@SuppressWarnings("unchecked")
	private Class<ThreadFactory> getFactoryType(String value)
			throws ThreadsException {
		try {
			return (Class<ThreadFactory>) Class.forName(value);
		} catch (ClassNotFoundException e) {
			throw log.threadFactoryNotFound(e, value);
		}
	}

	private ThreadFactory createFactory(Class<? extends ThreadFactory> type)
			throws ThreadsException {
		try {
			return ConstructorUtils.invokeConstructor(type);
		} catch (NoSuchMethodException e) {
			throw log.noDefaultCtor(e, type);
		} catch (IllegalAccessException e) {
			throw log.illegalAccessCtor(e, type);
		} catch (InvocationTargetException e) {
			throw log.exceptionCtor(e.getCause(), type);
		} catch (InstantiationException e) {
			throw log.instantiationErrorFactory(e, type);
		}
	}
}
