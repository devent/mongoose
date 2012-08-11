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
package com.anrisoftware.groovybash.buildins.runbuildin;

import com.anrisoftware.groovybash.core.Buildin;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the run build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RunModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Buildin.class).to(RunBuildin.class);
		install(new FactoryModuleBuilder().implement(OutputTask.class,
				OutputTask.class).build(OutputTaskFactory.class));
		install(new FactoryModuleBuilder().implement(ErrorTask.class,
				ErrorTask.class).build(ErrorTaskFactory.class));
	}
}
