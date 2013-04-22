package com.anrisoftware.mongoose.threads;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Loads the thread pool properties from a properties file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PropertiesThreads implements Threads {

	private final PropertiesThreadsLogger log;

	private final CachedThreadingPropertiesFactory cachedFactory;

	private final ThreadingPropertiesFactory propertiesFactory;

	private final FixedThreadingPropertiesFactory fixedFactory;

	private final ContextProperties properties;

	private final SingleThreadingPropertiesFactory singleFactory;

	private ExecutorService executor;

	private String name;

	@Inject
	PropertiesThreads(PropertiesThreadsLogger logger,
			ThreadingPropertiesFactory propertiesFactory,
			CachedThreadingPropertiesFactory cachedThreadingPropertiesFactory,
			FixedThreadingPropertiesFactory fixedThreadingPropertiesFactory,
			SingleThreadingPropertiesFactory singleThreadingPropertiesFactory,
			@Named("threads-properties") ContextProperties p) {
		this.log = logger;
		this.propertiesFactory = propertiesFactory;
		this.properties = p;
		this.cachedFactory = cachedThreadingPropertiesFactory;
		this.fixedFactory = fixedThreadingPropertiesFactory;
		this.singleFactory = singleThreadingPropertiesFactory;
	}

	@Override
	public void setName(String name) throws ThreadsException {
		log.checkName(this, name);
		String oldValue = this.name;
		this.name = name;
		if (!name.equals(oldValue)) {
			this.executor = createExecutor();
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

	@Override
	public void execute(Runnable command) {
		executor.execute(command);
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
		return executor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
