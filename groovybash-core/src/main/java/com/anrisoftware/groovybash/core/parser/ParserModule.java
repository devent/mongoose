/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.core.parser;

import static com.google.common.io.Resources.getResource;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;

import com.anrisoftware.groovybash.core.factories.BashParserFactory;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the bash parser factory and provides the parser properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ParserModule extends AbstractModule {

	private static final URL PARSER_PROPERTIES = getResource(
			ParserModule.class, "parser.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(BashParser.class,
				BashParser.class).build(BashParserFactory.class));
	}

	/**
	 * Provides the parser properties from the resource
	 * {@code parser.properties}.
	 * 
	 * @return the {@link Properties}.
	 * 
	 * @throws IOException
	 *             if there was an error reading the properties resource.
	 */
	@Provides
	@Named("parser-properties")
	public Properties getParserProperties() throws IOException {
		return new ContextPropertiesFactory(this)
				.fromResource(PARSER_PROPERTIES);
	}
}
