package com.anrisoftware.groovybash.core.parser;

import com.anrisoftware.groovybash.core.factories.BashParserFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(BashParser.class,
				BashParser.class).build(BashParserFactory.class));
	}
}
