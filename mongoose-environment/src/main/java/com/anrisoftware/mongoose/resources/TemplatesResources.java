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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;

/**
 * Returns the templates resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TemplatesResources {

	private final TemplatesFactory templatesFactory;

	private final Map<String, Templates> cache;

	private final TemplatesDelegate templatesDelegate;

	private ClassLoader classLoader;

	/**
	 * Sets the templates resources factory.
	 */
	@Inject
	TemplatesResources(TemplatesFactory templatesFactory,
			TemplatesDelegate textsDelegate) {
		this.templatesFactory = templatesFactory;
		this.cache = new HashMap<String, Templates>();
		this.classLoader = getClass().getClassLoader();
		this.templatesDelegate = textsDelegate;
	}

	/**
	 * Sets the class loaders that is used to load the templates resources.
	 * 
	 * @param classLoader
	 *            the {@link ClassLoader}.
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Missing property is used as the base name of the templates resources.
	 */
	public Object propertyMissing(String name) {
		Templates resource = getCachedResource(name);
		return templatesDelegate.setDelegate(resource);
	}

	private Templates getCachedResource(String name) {
		Templates resource = cache.get(name);
		if (resource == null) {
			resource = templatesFactory.create(name, classLoader);
			cache.put(name, resource);
		}
		return resource;
	}
}
