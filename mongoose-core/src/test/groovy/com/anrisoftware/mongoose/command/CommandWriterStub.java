package com.anrisoftware.mongoose.command;

/**
 * Command stub that writes data to the output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandWriterStub extends CommandStub {

	private final byte[] writeData;

	/**
	 * @param writeData
	 *            the data to write.
	 */
	public CommandWriterStub(String writeData) {
		this.writeData = writeData.getBytes();
	}

	@Override
	public String getTheName() {
		return "writer";
	}

	@Override
	protected void doCall() throws Exception {
		super.doCall();
		getOutput().write(writeData);
		getOutput().flush();
	}
}
