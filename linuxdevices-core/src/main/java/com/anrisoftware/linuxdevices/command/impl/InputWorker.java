package com.anrisoftware.linuxdevices.command.impl;

import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Read data from an input and write the data to an output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class InputWorker implements Runnable {

	private final InputStream stream;

	private final OutputStream processInput;

	private final int bufferSize;

	private IOException ioException;

	private InterruptedException interruptedException;

	/**
	 * Sets the input stream of the process and the stream with the data.
	 * 
	 * @param stream
	 *            the {@link OutputStream} of the input of the process.
	 * 
	 * @param output
	 *            the {@link InputStream} from which to read the data.
	 */
	@Inject
	InputWorker(@Assisted OutputStream stream, @Assisted InputStream inputStream) {
		this.stream = inputStream;
		this.bufferSize = 1024;
		this.processInput = stream;
	}

	/**
	 * Start reading data from the input and write the data to the output.
	 */
	@Override
	public void run() {
		try {
			writeProcessInput();
		} catch (IOException e) {
			ioException = e;
		} catch (InterruptedException e) {
			interruptedException = e;
		}
	}

	private void writeProcessInput() throws IOException, InterruptedException {
		byte[] buffer = new byte[bufferSize];
		int read;
		while ((read = stream.read(buffer)) != -1) {
			if (currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			processInput.write(buffer, 0, read);
		}
		processInput.flush();
	}

	/**
	 * Returns the I/O exception that was thrown.
	 * 
	 * @return the {@link IOException} thrown while reading or writing the data
	 *         or {@code null} if no exception was thrown.
	 */
	public IOException getIoException() {
		return ioException;
	}

	/**
	 * Returns the interrupted exception that was thrown.
	 * 
	 * @return the {@link InterruptedException} thrown while reading or writing
	 *         the data or {@code null} if no exception was thrown.
	 */
	public InterruptedException getInterruptedException() {
		return interruptedException;
	}
}
