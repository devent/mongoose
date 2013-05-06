package com.anrisoftware.mongoose.threads;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.mongoose.api.commans.ListenableFuture;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Loads the thread pool properties from a properties file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PropertiesThreads implements Threads {

	private final PropertiesThreadsLogger log;

	private final CachedThreadingPropertiesFactory cachedFactory;

	private final ThreadingPropertiesFactory propertiesFactory;

	private final FixedThreadingPropertiesFactory fixedFactory;

	private final SingleThreadingPropertiesFactory singleFactory;

	private final ThreadsWatchdog watchdog;

	private ContextProperties properties;

	private String name;

	private ExecutorService executor;

	/**
	 * @see PropertiesThreadsFactory#create(Properties, String)
	 */
	@AssistedInject
	PropertiesThreads(PropertiesThreadsLogger logger,
			ThreadingPropertiesFactory propertiesFactory,
			ThreadsWatchdog threadsWatchdog,
			CachedThreadingPropertiesFactory cachedThreadingPropertiesFactory,
			FixedThreadingPropertiesFactory fixedThreadingPropertiesFactory,
			SingleThreadingPropertiesFactory singleThreadingPropertiesFactory,
			@Assisted Properties properties, @Assisted String name)
			throws ThreadsException {
		this(logger, propertiesFactory, threadsWatchdog,
				cachedThreadingPropertiesFactory,
				fixedThreadingPropertiesFactory,
				singleThreadingPropertiesFactory);
		setProperties(properties);
		setName(name);
	}

	/**
	 * @see PropertiesThreadsFactory#create()
	 */
	@Inject
	@AssistedInject
	PropertiesThreads(PropertiesThreadsLogger logger,
			ThreadingPropertiesFactory propertiesFactory,
			ThreadsWatchdog threadsWatchdog,
			CachedThreadingPropertiesFactory cachedThreadingPropertiesFactory,
			FixedThreadingPropertiesFactory fixedThreadingPropertiesFactory,
			SingleThreadingPropertiesFactory singleThreadingPropertiesFactory) {
		this.log = logger;
		this.propertiesFactory = propertiesFactory;
		this.watchdog = threadsWatchdog;
		this.cachedFactory = cachedThreadingPropertiesFactory;
		this.fixedFactory = fixedThreadingPropertiesFactory;
		this.singleFactory = singleThreadingPropertiesFactory;
	}

	/**
	 * Sets the properties for the thread pool.
	 * 
	 * @param properties
	 *            the {@link Properties}.
	 */
	public void setProperties(Properties properties) {
		this.properties = new ContextProperties(this, properties);
	}

	@Override
	public void setName(String name) throws ThreadsException {
		log.checkName(this, name);
		String oldValue = this.name;
		this.name = name;
		if (!name.equals(oldValue)) {
			executor = createExecutor();
			watchdog.setExecutor(executor);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	private ExecutorService createExecutor() throws ThreadsException {
		ThreadingProperties p = propertiesFactory.create(properties, name);
		ThreadFactory factory = p.getThreadFactory(null);
		ThreadingPolicy policy = p.getPolicy();
		switch (policy) {
		case CACHED:
			return createCachedPool(factory);
		case FIXED:
			return createFixedPool(factory);
		case SINGLE:
			return createSinglePool(factory);
		default:
			throw log.invalidPolicy(this, policy);
		}
	}

	private ExecutorService createSinglePool(ThreadFactory factory) {
		return singleFactory.create(properties, name).createExecutorService(
				factory);
	}

	private ExecutorService createCachedPool(ThreadFactory factory) {
		return cachedFactory.create(properties, name).createExecutorService(
				factory);
	}

	private ExecutorService createFixedPool(ThreadFactory factory) {
		FixedThreadingProperties fixedProperties = fixedFactory.create(
				properties, name);
		int maxThreads = fixedProperties.getMaxThreads();
		return fixedProperties.createExecutorService(factory, maxThreads);
	}

	/**
	 * Unsupported.
	 */
	@Override
	public void execute(Runnable command) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return watchdog.submit(task);
	}

	@Override
	public <V> ListenableFuture<V> submit(Callable<V> callable,
			PropertyChangeListener... listeners) {
		return watchdog.submit(callable, listeners);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return watchdog.submit(task, result);
	}

	@Override
	public <V> ListenableFuture<V> submit(Runnable runable, V result,
			PropertyChangeListener... listeners) {
		return watchdog.submit(runable, result, listeners);
	}

	@Override
	public Future<?> submit(Runnable runable) {
		return watchdog.submit(runable, null);
	}

	/**
	 * Unsupported.
	 */
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported.
	 */
	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	/**
	 * Unsupported.
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported.
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Future<?>> getTasks() {
		return watchdog.getTasks();
	}

	@Override
	public void waitForTasks() throws InterruptedException {
		watchdog.waitForTasks();
	}

	@Override
	public List<Future<?>> waitForTasks(Duration timeout)
			throws InterruptedException {
		return watchdog.waitForTasks(timeout);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
