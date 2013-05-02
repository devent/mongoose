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
package com.anrisoftware.mongoose.parser;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the bash parser factory and provides the parser properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ParserModule extends AbstractModule {

	private static final URL PARSER_PROPERTIES = ParserModule.class
			.getResource("/parser.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(ScriptParser.class,
				ScriptParser.class).build(ScriptParserFactory.class));
	}

	/**
	 * Provides the parser properties from the resource
	 * {@code parser.properties}. The properties file can have the properties:
	 * 
	 * <dl>
	 * 
	 * <dt>{@code com.anrisoftware.mongoose.parser.script_package_to_classpath}</dt>
	 * <dd>If the script package should be added to the script class path.
	 * Default to {@code true}.</dd>
	 * 
	 * <dt>{@code com.anrisoftware.mongoose.parser.star_imports}</dt>
	 * <dd>A list of packages that should be imported in the script.</dd>
	 * 
	 * <dt>{@code com.anrisoftware.groovybash.parser.imports}</dt>
	 * <dd>A list of classes that should be imported in the script.</dd>
	 * </dl>
	 * 
	 * @return the {@link ContextProperties}.
	 * 
	 * @throws IOException
	 *             if there was an error reading the properties resource.
	 */
	@Provides
	@Singleton
	@Named("parser-properties")
	public ContextProperties getParserProperties() throws IOException {
		return new ContextPropertiesFactory(this).withProperties(
				System.getProperties()).fromResource(PARSER_PROPERTIES);
	}
}
