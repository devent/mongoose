package com.anrisoftware.groovybash.buildins.cdbuildin;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.groovybash.buildins.AbstractBuildin;
import com.anrisoftware.groovybash.buildins.StandardStreams;
import com.anrisoftware.groovybash.buildins.returns.ReturnCodeFactory;
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

	private final ReturnCodeFactory returnCodeFactory;

	private CdBuildin buildin;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	CdBuildin(StandardStreams streams, ReturnCodeFactory returnCodeFactory) {
		super(streams);
		this.buildin = this;
		this.returnCodeFactory = returnCodeFactory;
	}

	protected CdBuildin(CdBuildin parent) {
		super(parent);
		this.returnCodeFactory = parent.returnCodeFactory;
	}

	@Override
	public ReturnValue call() throws Exception {
		super.call();
		if (getArgs().length == 0) {
			buildin = new UserHomeCd(this);
		}
		return buildin.callBuildin();
	}

	ReturnValue callBuildin() throws Exception {
		return returnCodeFactory.createSuccess();
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
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
