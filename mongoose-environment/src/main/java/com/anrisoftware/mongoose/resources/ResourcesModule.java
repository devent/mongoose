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

import com.anrisoftware.resources.binary.binaries.BinariesResourcesModule;
import com.anrisoftware.resources.binary.maps.BinariesDefaultMapsModule;
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule;
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule;
import com.anrisoftware.resources.templates.worker.STWorkerModule;
import com.anrisoftware.resources.texts.maps.TextsDefaultMapsModule;
import com.anrisoftware.resources.texts.texts.TextsResourcesCharsetModule;
import com.anrisoftware.resources.texts.texts.TextsResourcesModule;
import com.google.inject.AbstractModule;

/**
 * Install the resources modules.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ResourcesModule extends AbstractModule {

	@Override
	protected void configure() {
		installTextsResources();
		installSTResources();
	}

	private void installTextsResources() {
		install(new BinariesResourcesModule());
		install(new BinariesDefaultMapsModule());
		install(new TextsResourcesModule());
		install(new TextsDefaultMapsModule());
		install(new TextsResourcesCharsetModule());
	}

	private void installSTResources() {
		install(new TemplatesDefaultMapsModule());
		install(new STWorkerModule());
		install(new STDefaultPropertiesModule());
	}
}
