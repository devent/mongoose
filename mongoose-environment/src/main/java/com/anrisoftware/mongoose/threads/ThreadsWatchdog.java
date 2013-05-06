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

import com.anrisoftware.mongoose.api.commans.ListenableFuture;
import com.anrisoftware.mongoose.api.commans.ListenableFuture.Status;
import com.anrisoftware.mongoose.command.DefaultListenableFuture;

/**
 * Keeps track of the submitted tasks.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ThreadsWatchdog {

	private final ThreadsWatchdogLogger log;

	private final List<Future<?>> tasks;

	private ExecutorService executor;

	private boolean notified;

	@Inject
	ThreadsWatchdog(ThreadsWatchdogLogger logger) {
		this.log = logger;
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
	 * @see Threads#submit(Callable, PropertyChangeListener...)
	 */
	public <V> DefaultListenableFuture<V> submit(Callable<V> callable,
			PropertyChangeListener... listeners) {
		DefaultListenableFuture<V> futureTask;
		futureTask = new DefaultListenableFuture<V>(callable);
		return submit(futureTask, listeners);
	}

	/**
	 * @see Threads#submit(Runnable, Object, PropertyChangeListener...)
	 */
	public <V> ListenableFuture<V> submit(Runnable runable, V result,
			PropertyChangeListener... listeners) {
		DefaultListenableFuture<V> futureTask;
		futureTask = new DefaultListenableFuture<V>(runable, result);
		return submit(futureTask, null);
	}

	private <V> DefaultListenableFuture<V> submit(
			final DefaultListenableFuture<V> task,
			PropertyChangeListener[] listeners) {
		for (PropertyChangeListener l : listeners) {
			task.addPropertyChangeListener(l);
		}
		task.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Status status = (Status) evt.getNewValue();
				if (status == Status.DONE) {
					tasks.remove(task);
					log.taskDone(task);
					unlockWait();
				}
			}

		});
		executor.submit(task);
		tasks.add(task);
		log.taskSubmitted(task);
		return task;
	}

	private void unlockWait() {
		synchronized (this) {
			notified = true;
			notify();
		}
	}

	/**
	 * @see Threads#getTasks()
	 * 
	 * @return a copy of the submitted tasks.
	 */
	public List<Future<?>> getTasks() {
		synchronized (tasks) {
			return new ArrayList<Future<?>>(tasks);
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
