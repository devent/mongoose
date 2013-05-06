package com.anrisoftware.mongoose.api.commans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Future;

/**
 * Informs property change listener when the task is finish.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ListenableFuture<V> extends Future<V> {

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

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(String,
	 *      PropertyChangeListener)
	 */
	void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

}
