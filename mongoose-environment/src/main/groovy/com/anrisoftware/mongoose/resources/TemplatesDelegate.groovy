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
package com.anrisoftware.mongoose.resources

import com.anrisoftware.resources.templates.api.Templates

/**
 * Delegates for the user specified method to return the template
 * from the template resource.
 * <p>
 * The property access example:
 * <pre>
 * TEMPLATES.TextsName.the_resource(args)
 * </pre>
 * Is delegated to: 
 * <pre>
 * resource.getResource("the_resource").getText(args)
 * </pre>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TemplatesDelegate {

	/**
	 * Sets the delegate.
	 * 
	 * @param resource
	 * 			  the {@link Templates} resource.
	 * 
	 * @param locale
	 * 			  the {@link Locale} of the resource.
	 * 
	 * @return the {@link Templates} resource with the method delegate.
	 */
	Templates setDelegate(Templates resource, Locale locale) {
		resource.metaClass.methodMissing = { name, args ->
			resource.getResource(name, locale).getText(args)
		}
		return resource
	}
}
