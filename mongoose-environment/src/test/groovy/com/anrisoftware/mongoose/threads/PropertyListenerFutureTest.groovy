package com.anrisoftware.mongoose.threads

import java.beans.PropertyChangeListener
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import org.junit.Test

import com.anrisoftware.mongoose.api.commans.ListenableFuture.Status
import com.anrisoftware.mongoose.command.DefaultListenableFuture

/**
 * @see PropertyListenerFuture
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PropertyListenerFutureTest {

	@Test
	void "submit listener task"() {
		def taskStatus = null
		def listener = { evt -> taskStatus = evt.newValue } as PropertyChangeListener
		boolean taskRun = false
		def task = { taskRun = true }
		def future = new DefaultListenableFuture(task)
		future.addPropertyChangeListener listener
		def service = Executors.newCachedThreadPool()

		service.submit(future)
		service.shutdown()
		service.awaitTermination(100, TimeUnit.MILLISECONDS)
		assert taskRun == true
		assert taskStatus == Status.DONE
	}
}
