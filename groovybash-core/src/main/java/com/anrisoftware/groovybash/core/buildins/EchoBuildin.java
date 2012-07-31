package com.anrisoftware.groovybash.core.buildins;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.Map;

import javax.inject.Inject;

/**
 * The build-in command {@code echo [nonewline] arguments…}. Outputs the
 * {@code arguments…}, separated by spaces. If {@code nonewline} was specified
 * the output is not followed by a newline, otherwise a newline is following the
 * output.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoBuildin extends AbstractBuildin {

	private static final String SEPARATOR = " ";

	private EchoBuildin buidlin;

	/**
	 * Sets the standard input and output streams.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} that returns the standard input
	 *            and output streams.
	 */
	@Inject
	EchoBuildin(StandardStreams streams) {
		super(streams);
		this.buidlin = this;
	}

	@Override
	public EchoBuildin call() {
		return buidlin.callBuildin();
	}

	EchoBuildin callBuildin() {
		getOutputStream().println(join(getArgs(), SEPARATOR));
		getOutputStream().flush();
		return this;
	}

	@Override
	public void setArguments(Map<?, ?> flags, Object[] args) {
		super.setArguments(flags, args);
		if (getFlag("nonewline", false)) {
			buidlin = nonewline();
		}
	}

	/**
	 * Returns the name {@code echo}.
	 */
	@Override
	public String getName() {
		return "echo";
	}

	/**
	 * Specify that no newline should be followed the specified arguments.
	 * 
	 * @return this {@link EchoBuildin}.
	 */
	public EchoBuildin nonewline() {
		return new EchoNoNewLine(this);
	}

	/**
	 * The {@code echo} build-in command that will not output a newline after
	 * the arguments.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 1.0
	 */
	private static class EchoNoNewLine extends EchoBuildin {

		private EchoNoNewLine(AbstractBuildin parent) {
			super(new StandardStreams(parent.getInputStream(),
					parent.getOutputStream(), parent.getErrorStream()));
			setArguments(parent.getArgs());
		}

		@Override
		EchoBuildin callBuildin() {
			getOutputStream().print(join(getArgs(), SEPARATOR));
			getOutputStream().flush();
			return this;
		}
	}

	@Override
	public String toString() {
		return "echo";
	}
}
