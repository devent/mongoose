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
package com.anrisoftware.groovybash.parser

import com.anrisoftware.groovybash.core.Environment

/**
 * Sets the delegate for the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParserMetaClass {

	/**
	 * Sets the environment for the specified script. All missing methods or
	 * missing properties are delegated to the environment.
	 * 
	 * @param script
	 * 			  the {@link Script}.
	 * 
	 * @param environment
	 * 			  the {@link Environment}.
	 * 
	 * @return the {@link Script} with the set delegate.
	 */
	Script setDelegate(Script script, Environment environment) {
		script.metaClass.methodMissing = { name, args ->
			environment.invokeMethod(name, args)
		}
		script.metaClass.propertyMissing = { name ->
			environment.getProperty(name)
		}
		return script
	}
}
