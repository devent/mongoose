package com.anrisoftware.mongoose.threads

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

/**
 * @see ThreadsWatchdog
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ThreadsWatchdogTest {

	@Test(timeout = 2000l)
	void "submit callable task"() {
		boolean taskRun = false
		def task = { Thread.sleep 1000; taskRun = true }
		def future = watchdog.submit(task)

		watchdog.waitForTasks()
		assert taskRun == true
	}

	@Test(timeout = 3000l)
	void "submit callable task +short timeout"() {
		boolean taskRun = false
		def task = { Thread.sleep 2000; taskRun = true }
		def future = watchdog.submit(task)

		def notFinished = watchdog.waitForTasks Duration.parse("PT1S")
		assert notFinished.size() == 1
		assert taskRun == false
	}

	@Test(timeout = 3000l)
	void "submit callable task +long timeout"() {
		boolean taskRun = false
		def task = { Thread.sleep 1000; taskRun = true }
		def future = watchdog.submit(task)

		def notFinished = watchdog.waitForTasks Duration.parse("PT2S")
		assert notFinished.size() == 0
		assert taskRun == true
	}

	ThreadsWatchdog watchdog

	@Before
	void setupWatchdog() {
		watchdog = new ThreadsWatchdog()
		watchdog.setExecutor service
	}

	static ExecutorService service

	@BeforeClass
	static void setupService() {
		service = Executors.newCachedThreadPool()
	}
}
