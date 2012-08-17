package com.anrisoftware.groovybash.resources;

import groovy.lang.GroovyObjectSupport;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.resources.api.Texts;
import com.anrisoftware.resources.api.TextsFactory;
import com.google.common.collect.Maps;

public class TextsResources extends GroovyObjectSupport {

	private final TextsFactory textsFactory;

	private final Map<String, Texts> resources;

	private final TextsDelegate textsDelegate;

	private ClassLoader classLoader;

	@Inject
	TextsResources(TextsFactory textsFactory, TextsDelegate textsDelegate) {
		this.textsFactory = textsFactory;
		this.resources = Maps.newHashMap();
		this.classLoader = getClass().getClassLoader();
		this.textsDelegate = textsDelegate;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

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
