package com.anrisoftware.groovybash.buildins.cdbuildin;

import com.anrisoftware.groovybash.buildins.returns.ReturnsModule;
import com.anrisoftware.groovybash.buildins.utils.ReadUntilCharacterModule;
import com.anrisoftware.groovybash.core.Buildin;
import com.google.inject.AbstractModule;

/**
 * Binds the cd build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CdModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Buildin.class).to(CdBuildin.class);
		install(new ReadUntilCharacterModule());
		install(new ReturnsModule());
	}
}
