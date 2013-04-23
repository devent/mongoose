package com.anrisoftware.mongoose.environment

import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.globalpom.strings.MapToTableString

/**
 * @see System#getenv()
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class EnvTest {

	@Test
	void "print environment variables"() {
		log.info MapToTableString.mapToString(System.getenv())
	}
}
