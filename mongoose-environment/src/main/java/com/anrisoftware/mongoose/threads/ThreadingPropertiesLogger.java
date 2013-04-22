package com.anrisoftware.mongoose.threads;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.concurrent.ThreadFactory;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ThreadingProperties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ThreadingPropertiesLogger extends AbstractLogger {

	private static final String INSTANTIATION_ERROR_MESSAGE = "Instantiation error of thread factory %s.";
	private static final String INSTANTIATION_ERROR = "Instantiation error of thread factory";
	private static final String ERROR_DEFAULT_CTOR_MESSAGE = "Error in default constructor of thread factory %s.";
	private static final String ERROR_DEFAULT_CTOR = "Error in default constructor of thread factory";
	private static final String ILLEGAL_ACCESS_MESSAGE = "Illegal access to default constructor of thread factory %s.";
	private static final String ILLEGAL_ACCESS = "Illegal access to default constructor of thread factory";
	private static final String NAME = "name";
	private static final String THREAD_FACTORY_NOT_FOUND_MESSAGE = "Thread factory %s not found";
	private static final String THREAD_FACTORY_NOT_FOUND = "Thread factory not found";
	private static final String THREAD_FACTORY_NULL = "No thread factory found.";
	private static final String THREADING_POLICY_NULL = "No threading policy found.";
	private static final String TYPE = "type";
	private static final String NO_DEFAULT_CONSTRUCTOR_MESSAGE = "No default constructor for type %s available.";
	private static final String NO_DEFAULT_CONSTRUCTOR = "No default constructor available";

	/**
	 * Create logger for {@link ThreadingProperties}.
	 */
	public ThreadingPropertiesLogger() {
		super(ThreadingProperties.class);
	}

	ThreadsException threadFactoryNotFound(ClassNotFoundException e,
			String value) {
		return logException(
				new ThreadsException(THREAD_FACTORY_NOT_FOUND, e).addContext(
						NAME, value), THREAD_FACTORY_NOT_FOUND_MESSAGE, value);
	}

	ThreadsException noDefaultCtor(NoSuchMethodException e, Class<?> type) {
		return logException(
				new ThreadsException(NO_DEFAULT_CONSTRUCTOR, e).addContext(
						TYPE, type), NO_DEFAULT_CONSTRUCTOR_MESSAGE, type);
	}

	ThreadsException illegalAccessCtor(IllegalAccessException e, Class<?> type) {
		return logException(
				new ThreadsException(ILLEGAL_ACCESS, e).addContext(TYPE, type),
				ILLEGAL_ACCESS_MESSAGE, type);
	}

	ThreadsException exceptionCtor(Throwable e, Class<?> type) {
		return logException(
				new ThreadsException(ERROR_DEFAULT_CTOR, e).addContext(TYPE,
						type), ERROR_DEFAULT_CTOR_MESSAGE, type);
	}

	ThreadsException instantiationErrorFactory(InstantiationException e,
			Class<?> type) {
		return logException(
				new ThreadsException(INSTANTIATION_ERROR, e).addContext(TYPE,
						type), INSTANTIATION_ERROR_MESSAGE, type);
	}

	void checkPolicy(ThreadingProperties p, ThreadingPolicy value) {
		notNull(value, THREADING_POLICY_NULL);
	}

	void checkThreadFactory(ThreadingProperties p, ThreadFactory value) {
		notNull(value, THREAD_FACTORY_NULL);
	}

}
