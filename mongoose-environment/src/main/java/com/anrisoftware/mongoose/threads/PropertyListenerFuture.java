package com.anrisoftware.mongoose.threads;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Informs property change listener when the task is finish.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PropertyListenerFuture<V> extends FutureTask<V> {

	/**
	 * The status of the task property.
	 */
	public static final String STATUS_PROPERTY = "status";

	/**
	 * Status of the task.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 1.0
	 */
	public enum Status {

		/**
		 * The task is currently running.
		 */
		RUNNING,

		/**
		 * The task is finished.
		 */
		DONE;
	}

	private final PropertyChangeSupport propertySupport;

	private Status status;

	/**
	 * @see FutureTask#FutureTask(Callable)
	 */
	public PropertyListenerFuture(Callable<V> callable) {
		super(callable);
		this.propertySupport = new PropertyChangeSupport(this);
		this.status = Status.RUNNING;
	}

	/**
	 * @see FutureTask#FutureTask(Runnable, Object)
	 */
	public PropertyListenerFuture(Runnable runnable, V result) {
		super(runnable, result);
		this.propertySupport = new PropertyChangeSupport(this);
	}

	@Override
	protected void done() {
		Status oldValue = this.status;
		status = Status.DONE;
		propertySupport.firePropertyChange(STATUS_PROPERTY, oldValue, status);
	}

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
	 * @see #STATUS_PROPERTY
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
	 * @see #STATUS_PROPERTY
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(String,
	 *      PropertyChangeListener)
	 * @see #STATUS_PROPERTY
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(String,
	 *      PropertyChangeListener)
	 * @see #STATUS_PROPERTY
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(propertyName, listener);
	}

}
