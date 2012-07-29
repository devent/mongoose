package com.anrisoftware.groovybash.core.buildins;

import com.anrisoftware.groovybash.core.api.Buildin;
import com.google.inject.AbstractModule;

/**
 * Binds the echo build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EchoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Buildin.class).to(EchoBuildin.class);
	}
}
