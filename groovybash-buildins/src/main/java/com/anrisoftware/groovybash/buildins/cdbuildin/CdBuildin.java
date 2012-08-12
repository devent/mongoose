package com.anrisoftware.groovybash.buildins.cdbuildin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.core.Environment;
import com.anrisoftware.groovybash.core.ReturnValue;

/**
 * The build-in command {@code cd [DIR]}. Change the current working directory
 * to the specified directory {@code DIR}. {@code DIR} defaults to the user home
 * directory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CdBuildin extends AbstractBuildin {

	private CdWorker worker;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	CdBuildin(StandardStreams streams) {
		super(streams);
	}

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		this.worker = createChangeToUserHomeWorker();
	}

	private CdWorker createChangeToUserHomeWorker() {
		return new CdWorker() {

			@Override
			public void changeDirectory() {
				Environment e = getEnvironment();
				e.setWorkingDirectory(e.getUserHome());
			}
		};
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		worker.changeDirectory();
		return returnCodeFactory.createSuccess();
	}

	ReturnValue callBuildin() throws Exception {
		return returnCodeFactory.createSuccess();
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (args.length == 1) {
			worker = createChangeDirectoryWorker();
		}
		if (getFlag("in", null) != null && args.length == 0) {
			worker = createReadDirectoryFromInputStream();
		}
	}

	private CdWorker createReadDirectoryFromInputStream() {
		return new CdWorker() {

			@Override
			public void changeDirectory() throws IOException {
				String name = readDirectory();
				File directory = new File(name);
				getEnvironment().setWorkingDirectory(directory);
			}
		};
	}

	private String readDirectory() throws IOException {
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(getInputStream()));
		return reader.readLine();
	}

	private CdWorker createChangeDirectoryWorker() {
		return new CdWorker() {

			@Override
			public void changeDirectory() {
				File directory = asFile(getArgs()[0]);
				getEnvironment().setWorkingDirectory(directory);
			}
		};
	}

	private File asFile(Object object) {
		if (object instanceof File) {
			return (File) object;
		}
		return new File(object.toString());
	}

	/**
	 * Returns the name {@code cd}.
	 */
	@Override
	public String getName() {
		return "cd";
	}

	@Override
	public String toString() {
		return getName();
	}
}
