package com.anrisoftware.linuxdevices.command.impl;

import com.anrisoftware.linuxdevices.command.api.CheckIsRootUser;
import com.anrisoftware.linuxdevices.command.api.CommandWorker;
import com.anrisoftware.linuxdevices.command.api.SuCommandWorker;
import com.anrisoftware.linuxdevices.command.factories.CommandWorkerFactory;
import com.anrisoftware.linuxdevices.command.factories.SuCommandWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the command worker factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class CommandModule extends AbstractModule {

	@Override
	protected void configure() {
		installCommandWorker();
		installAsRootWorker();
	}

	private void installCommandWorker() {
		install(new FactoryModuleBuilder().implement(CommandWorker.class,
				CommandWorkerImpl.class).build(CommandWorkerFactory.class));
		install(new FactoryModuleBuilder().implement(OutputWorker.class,
				OutputWorker.class).build(OutputTaskFactory.class));
		install(new FactoryModuleBuilder().implement(InputWorker.class,
				InputWorker.class).build(InputTaskFactory.class));
	}

	private void installAsRootWorker() {
		install(new FactoryModuleBuilder().implement(SuCommandWorker.class,
				SuCommandWorkerImpl.class).build(SuCommandWorkerFactory.class));
		bind(CheckIsRootUser.class).to(CheckIsRootUserImpl.class);
	}

}
