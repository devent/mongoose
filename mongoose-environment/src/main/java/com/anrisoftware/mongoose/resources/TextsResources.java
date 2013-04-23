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
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;

/**
 * Returns the texts resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TextsResources {

	private final TextsFactory textsFactory;

	private final Map<String, Texts> cache;

	private final TextsDelegate textsDelegate;

	private ClassLoader classLoader;

	private Locale locale;

	/**
	 * Sets the text resources factory.
	 */
	@Inject
	TextsResources(TextsFactory textsFactory, TextsDelegate textsDelegate) {
		this.textsFactory = textsFactory;
		this.cache = new HashMap<String, Texts>();
		this.classLoader = getClass().getClassLoader();
		this.textsDelegate = textsDelegate;
		this.locale = Locale.getDefault();
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
	 * Sets the resources locale.
	 * 
	 * @param locale
	 *            the {@link Locale}.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Missing property is used as the base name of the text resources.
	 */
	public Object propertyMissing(String name) {
		Texts resource = getCachedResource(name);
		return textsDelegate.setDelegate(resource, locale);
	}

	private Texts getCachedResource(String name) {
		Texts resource = cache.get(name);
		if (resource == null) {
			resource = textsFactory.create(name, classLoader);
			cache.put(name, resource);
		}
		return resource;
	}
}
