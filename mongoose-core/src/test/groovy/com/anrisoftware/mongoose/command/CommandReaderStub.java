package com.anrisoftware.mongoose.command;

import org.apache.commons.io.IOUtils;

/**
 * Command stub that reads data from the input.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandReaderStub extends CommandStub {

	private String readData;

	@Override
	public String getTheName() {
		return "reader";
	}

	@Override
	protected void doCall() throws Exception {
		super.doCall();
		readData = IOUtils.toString(getInput());
	}

	/**
	 * Returns the red data.
	 */
	public String getReadData() {
		return readData;
	}
}
