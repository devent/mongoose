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
package com.anrisoftware.mongoose.resources;

import groovy.lang.GroovyObjectSupport;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.resources.TemplatesDelegate;
import com.anrisoftware.groovybash.resources.TextsDelegate;
import com.anrisoftware.resources.api.Templates;
import com.anrisoftware.resources.api.TemplatesFactory;
import com.google.common.collect.Maps;

/**
 * Returns the templates resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.3
 */
public class TemplatesResources extends GroovyObjectSupport {

	private final TemplatesFactory templatesFactory;

	private final Map<String, Templates> resources;

	private final TemplatesDelegate templatesDelegate;

	private ClassLoader classLoader;

	/**
	 * Sets the text resources factory.
	 * 
	 * @param templatesFactory
	 *            the {@link TemplatesFactory} that creates the text resources.
	 * 
	 * @param textsDelegate
	 *            the {@link TextsDelegate} that delegates missing properties to
	 *            the text resources.
	 */
	@Inject
	TemplatesResources(TemplatesFactory templatesFactory,
			TemplatesDelegate textsDelegate) {
		this.templatesFactory = templatesFactory;
		this.resources = Maps.newHashMap();
		this.classLoader = getClass().getClassLoader();
		this.templatesDelegate = textsDelegate;
	}

	/**
	 * Sets the class loaders that is used to load the text resources.
	 * 
	 * @param classLoader
	 *            the {@link ClassLoader}.
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Missing property is used as the base name of the text resources.
	 */
	@Override
	public Object getProperty(String name) {
		if (getMetaClass().hasProperty(this, name) != null) {
			return super.getProperty(name);
		}

		Templates resource = lazyCreateResource(name);
		return templatesDelegate.setDelegate(resource);
	}

	private Templates lazyCreateResource(String name) {
		Templates resource = resources.get(name);
		if (resource == null) {
			resource = templatesFactory.create(name, classLoader);
			resources.put(name, resource);
		}
		return resource;
	}
}
