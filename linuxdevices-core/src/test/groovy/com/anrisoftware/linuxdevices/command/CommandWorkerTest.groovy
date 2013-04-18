package com.anrisoftware.linuxdevices.command

import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.linuxdevices.command.factories.CommandWorkerFactory
import com.google.inject.Injector

/**
 * Unit test for {@link CommandWorker}
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@Slf4j
class CommandWorkerTest extends CommandTestUtils {

	@Before
	void createFactories() {
		commandWorkerFactory = injector.getInstance CommandWorkerFactory
	}

	@Test
	void "run echo command"() {
		def command = 'bash -c "echo -n Test"'
		def worker = commandWorkerFactory.create(command, streams)
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "Test"
	}

	@Test
	void "run sudo command"() {
		def command = 'sudo /bin/echo -n "Test"'
		def worker = commandWorkerFactory.create(command, streams)
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "Test"
	}

	@Test
	void "run echo command with custom environment set"() {
		def command = 'bash -c "echo -n $TEST"'
		def worker = commandWorkerFactory.create(command, streams)
		worker.environment = [TEST: "Test Environment"]
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "Test Environment"
	}

	@Test
	void "run echo command with custom environment"() {
		def command = 'bash -c "echo -n $TEST"'
		def worker = commandWorkerFactory.create(command, streams).
						withEnvironment([TEST: "Test Environment"])
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "Test Environment"
	}

	@Test
	void "run echo command with error output"() {
		def command = 'bash -xc "echo -n Test"'
		def worker = commandWorkerFactory.create(command, streams)
		worker()

		log.info "Output of command: ``{}''.", output
		log.info "Error of command: ``{}''.", error
		assertStringContent error, "+ echo -n Test\n"
	}

	@Test
	void "run echo command with redirect error set"() {
		def command = 'bash -xc "echo -n Test"'
		def worker = commandWorkerFactory.create(command, streams)
		worker.redirectErrorStream = true
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "+ echo -n Test\nTest"
	}

	@Test
	void "run echo command with redirect error"() {
		def command = 'bash -xc "echo -n Test"'
		def worker = commandWorkerFactory.create(command, streams).
						redirectErrorStream()
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "+ echo -n Test\nTest"
	}

	@Test
	void "check exit code"() {
		def command = 'bash -c "exit 1"'
		def worker = commandWorkerFactory.create(command, streams)
		worker()

		log.info "Exit code of command: ``{}''.", worker.exitCode
		assert worker.exitCode == 1
	}

	//@Test(timeout = 10000l)
	void "output input stream"() {
		def command = 'bash -c "cat | echo"'
		inputBuffer = 'Test\n'.bytes
		def worker = commandWorkerFactory.create(command, streams)
		worker()

		log.info "Output of command: ``{}''.", output
		assertStringContent output, "+ echo -n Test\nTest"
	}
}
