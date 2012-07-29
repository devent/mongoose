package com.anrisoftware.groovybash.core.buildins;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class BuildinsModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	/**
	 * Provides a default of the the standard input and output streams for
	 * build-ins commands.
	 * 
	 * @return the {@link StandardStreams} that returns the standard input and
	 *         output streams.
	 */
	@Provides
	protected StandardStreams getStandardStreams() {
		return new StandardStreams(System.in, System.out, System.err);
	}
}
