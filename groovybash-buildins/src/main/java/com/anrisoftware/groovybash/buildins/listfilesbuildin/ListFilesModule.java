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
package com.anrisoftware.groovybash.buildins.listfilesbuildin;

import com.anrisoftware.groovybash.core.Buildin;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the {@code listFiles} build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class ListFilesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Buildin.class).to(ListFilesBuildin.class);
		install(new FactoryModuleBuilder().implement(FilesReturnValue.class,
				FilesReturnValue.class).build(FilesReturnValueFactory.class));
		install(new FactoryModuleBuilder().implement(ListWorker.class,
				WildcardListWorker.class).build(WildcardListFactory.class));
	}
}
