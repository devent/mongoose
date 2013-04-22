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

import com.anrisoftware.groovybash.resources.TextsDelegate;
import com.anrisoftware.resources.api.Texts;
import com.anrisoftware.resources.api.TextsFactory;
import com.google.common.collect.Maps;

/**
 * Returns the texts resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.3
 */
public class TextsResources extends GroovyObjectSupport {

	private final TextsFactory textsFactory;

	private final Map<String, Texts> resources;

	private final TextsDelegate textsDelegate;

	private ClassLoader classLoader;

	/**
	 * Sets the text resources factory.
	 * 
	 * @param textsFactory
	 *            the {@link TextsFactory} that creates the text resources.
	 * 
	 * @param textsDelegate
	 *            the {@link TextsDelegate} that delegates missing properties to
	 *            the text resources.
	 */
	@Inject
	TextsResources(TextsFactory textsFactory, TextsDelegate textsDelegate) {
		this.textsFactory = textsFactory;
		this.resources = Maps.newHashMap();
		this.classLoader = getClass().getClassLoader();
		this.textsDelegate = textsDelegate;
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

		Texts resource = lazyCreateResource(name);
		return textsDelegate.setDelegate(resource);
	}

	private Texts lazyCreateResource(String name) {
		Texts resource = resources.get(name);
		if (resource == null) {
			resource = textsFactory.create(name, classLoader);
			resources.put(name, resource);
		}
		return resource;
	}
}
