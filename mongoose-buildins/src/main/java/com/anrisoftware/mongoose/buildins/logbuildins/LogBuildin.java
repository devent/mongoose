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
package com.anrisoftware.mongoose.buildins.logbuildins;

import groovy.util.Proxy;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.LoggerFactory;

import com.anrisoftware.mongoose.command.AbstractCommand;

/**
 * Create a logger.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LogBuildin extends AbstractCommand {

	private final LogBuildinLogger log;

	private String name;

	private Proxy proxy;

	@Inject
	LogBuildin(LogBuildinLogger logger) {
		this.log = logger;
	}

	/**
	 * Returns the name {@code log}.
	 */
	@Override
	public String getTheName() {
		return "log";
	}

	@Override
	protected void doCall() throws Exception {
		proxy = new Proxy();
		proxy.setAdaptee(LoggerFactory.getLogger(name));
	}

	@Override
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
		log.checkArgs(this, args);
		Object object = unnamedArgs.get(0);
		setName(object);
	}

	private void setName(Object object) {
		if (object instanceof Class) {
			this.name = ((Class<?>) object).getName();
		} else if (object instanceof String) {
			this.name = (String) object;
		} else {
			this.name = object.toString();
		}
	}

	/**
	 * Pass method invocations to the logger.
	 */
	public Object methodMissing(String name, Object args) {
		log.checkLogger(this, proxy);
		return proxy.invokeMethod(name, args);
	}

	/**
	 * Returns properties of the logger.
	 */
	public Object propertyMissing(String name) {
		log.checkLogger(this, proxy);
		return proxy.getProperty(name);
	}
}
