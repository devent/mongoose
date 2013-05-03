package com.anrisoftware.mongoose.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command stub.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandStub extends AbstractCommand {

	private static Logger log = LoggerFactory.getLogger(CommandStub.class);

	private boolean commandCalled;

	public CommandStub() {
		this.commandCalled = false;
	}

	public String getName() {
		return "stub";
	}

	@Override
	public String getTheName() {
		return "stub";
	}

	@Override
	protected void doCall() throws Exception {
		log.info("do call {}.", this);
		commandCalled = true;
	}

	/**
	 * Returns if the command was called.
	 * 
	 * @return {@code true} if the command was called, {@code false} if not.
	 */
	public boolean isCommandCalled() {
		return commandCalled;
	}
}
