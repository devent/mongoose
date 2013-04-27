package com.anrisoftware.mongoose.threads;

import static java.util.Collections.synchronizedList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.anrisoftware.mongoose.threads.PropertyListenerFuture.Status;

/**
 * Keeps track of the submitted tasks.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ThreadsWatchdog {

	private final List<Future<?>> tasks;

	private ExecutorService executor;

	private boolean notified;

	@Inject
	ThreadsWatchdog() {
		this.tasks = synchronizedList(new ArrayList<Future<?>>());
		this.notified = false;
	}

	/**
	 * Sets the executor service to submit tasks.
	 * 
	 * @param executor
	 *            the {@link ExecutorService}.
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * @see ExecutorService#submit(Callable)
	 */
	public <V> PropertyListenerFuture<V> submit(Callable<V> callable) {
		PropertyListenerFuture<V> futureTask;
		futureTask = new PropertyListenerFuture<V>(callable);
		return submit(futureTask);
	}

	/**
	 * @see ExecutorService#submit(Runnable, Object)
	 */
	public <V> Future<V> submit(Runnable runable, V result) {
		PropertyListenerFuture<V> futureTask;
		futureTask = new PropertyListenerFuture<V>(runable, result);
		return submit(futureTask);
	}

	private <V> PropertyListenerFuture<V> submit(
			PropertyListenerFuture<V> futureTask) {
		final Future<?> future = executor.submit(futureTask);
		tasks.add(future);
		futureTask.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Status status = (Status) evt.getNewValue();
				if (status == Status.DONE) {
					tasks.remove(future);
					unlockWait();
				}
			}

		});
		return futureTask;
	}

	private void unlockWait() {
		synchronized (this) {
			notified = true;
			notify();
		}
	}

	/**
	 * @see Threads#waitForTasks()
	 */
	public void waitForTasks() throws InterruptedException {
		while (tasks.size() > 0) {
			synchronized (this) {
				wait();
			}
		}
	}

	/**
	 * @see Threads#waitForTasks(Duration)
	 */
	public List<Future<?>> waitForTasks(Duration timeout)
			throws InterruptedException {
		while (tasks.size() > 0) {
			synchronized (this) {
				wait(timeout.getMillis());
				if (notified) {
					notified = false;
				} else {
					break;
				}
			}
		}
		return tasks;
	}

}
