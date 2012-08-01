package com.anrisoftware.groovybash.parameter

import groovy.util.logging.Slf4j

import com.google.inject.AbstractModule

/**
 * Binds the model classes.
 * 
 * @author Erwin Müller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ParameterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind Parameter to ParameterImpl
	}
}
