package com.anrisoftware.groovybash.core.buildins.runbuildin;

import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

class ErrorTask implements Runnable {

	private final InputStream processOutput;

	private final PrintStream output;

	private final int bufferSize;

	private Exception exception;

	@Inject
	ErrorTask(@Assisted Process process, @Assisted PrintStream output) {
		this.output = output;
		this.processOutput = process.getErrorStream();
		this.bufferSize = 1024;
	}

	@Override
	public void run() {
		try {
			readProcessOutput();
		} catch (Exception e) {
			exception = e;
		}
	}

	private void readProcessOutput() throws IOException, InterruptedException {
		byte[] buffer = new byte[bufferSize];
		int read;
		while (processOutput.available() > 0) {
			if (currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			read = processOutput.read(buffer);
			output.write(buffer, 0, read);
		}
	}

	public Exception getException() {
		return exception;
	}

}
