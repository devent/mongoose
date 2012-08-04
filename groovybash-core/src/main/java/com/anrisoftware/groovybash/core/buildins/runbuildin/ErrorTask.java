package com.anrisoftware.groovybash.core.buildins.runbuildin;

import static java.lang.Thread.currentThread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

class ErrorTask implements Runnable {

	private final BufferedInputStream processOutput;

	private final PrintStream output;

	private Exception exception;

	private final int bufferSize;

	@Inject
	ErrorTask(@Assisted Process process, @Assisted PrintStream output) {
		this.output = output;
		this.bufferSize = 1024;
		this.processOutput = new BufferedInputStream(process.getErrorStream());
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
		while ((read = processOutput.read(buffer)) != -1) {
			if (currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			output.write(buffer, 0, read);
		}
	}

	public Exception getException() {
		return exception;
	}

}
