package com.anrisoftware.groovybash.resources

import com.anrisoftware.resources.api.Texts

class TextsDelegate {

	/**
	 * Sets a delegate for the user specified properties to return the text
	 * from the text resource.
	 * 
	 * @param resource
	 * 			  the {@link Texts} resource.
	 * 
	 * @return the {@link Texts} resource with the property delegate.
	 */
	Texts setDelegate(Texts resource) {
		resource.metaClass.propertyMissing = { name ->
			resource.getResource(name).text
		}
		return resource
	}
}
