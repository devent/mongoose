package com.anrisoftware.linuxdevices.command

import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.linuxdevices.command.factories.CommandWorkerFactory
import com.anrisoftware.linuxdevices.command.factories.SuCommandWorkerFactory
import com.google.inject.Injector

/**
 * Unit test for {@link SuCommandWorker}
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@Slf4j
class SuCommandWorkerTest extends CommandTestUtils {

	@Before
	void createFactories() {
		asRootFactory = injector.getInstance SuCommandWorkerFactory
	}

	@Test
	void "run command as root with sudo"() {
		def command = '/bin/echo -n "Test"'
		def worker = asRootFactory.create(command, streams)
		worker()

		log.info "Output of command: ``{}'', ``{}''.", output, error
		assertStringContent output, "Test"
	}

	@Test
	void "run command as root with kdesu"() {
		def kdesu = "/usr/bin/kdesu -t -c {}"
		def command = '"echo -n Test"'
		def worker = asRootFactory.create(command, streams)
		worker.sudoCommand = kdesu
		worker()

		log.info "Output of command: ``{}'', ``{}''.", output, error
		assertStringContent output, "Test"
	}

	@Test(timeout = 6000l)
	void "run command as root with su"() {
		def kdesu = "/bin/su -c {}"
		def command = '"echo -n Test"'
		def worker = asRootFactory.create(command, streams)
		worker.sudoCommand = kdesu
		worker()

		log.info "Output of command: ``{}'', ``{}''.", output, error
		assertStringContent output, "Test"
	}
}
