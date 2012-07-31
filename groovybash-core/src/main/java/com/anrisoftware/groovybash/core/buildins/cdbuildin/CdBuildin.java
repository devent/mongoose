package com.anrisoftware.groovybash.core.buildins.cdbuildin;

import java.io.File;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;

/**
 * The build-in command {@code cd [DIR]}. Change the current working directory
 * to the specified directory {@code DIR}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CdBuildin extends AbstractBuildin {

	private CdBuildin buildin;

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
		this.buildin = this;
	}

	@Override
	public CdBuildin call() {
		if (getArgs().length == 0) {
			buildin = new UserHomeCd(this);
		}
		return buildin.callBuildin();
	}

	CdBuildin callBuildin() {
		return this;
	}

	@Override
	public void setArguments(Object[] args) {
		super.setArguments(args);
		if (args.length == 1) {
			buildin = new FileCd(this, new File(args[0].toString()));
		}
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
