package com.anrisoftware.mongoose.command

class CommandStub extends AbstractCommand {

	boolean called = false

	String name = "stub"

	@Override
	String getTheName() {
		name
	}

	@Override
	protected void doCall() throws Exception {
		called = true
	}
}
