package com.anrisoftware.groovybash.core.exceptions;

import static java.lang.String.format;

/**
 * Thrown if the application encounters any errors.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class CommandException extends Exception {

	public CommandException(Throwable cause, String format, Object... args) {
		super(format(format, args), cause);
	}

	public CommandException(String format, Object... args) {
		super(format(format, args));
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (getCause() != null) {
			message = formatWithCause(message);
		} else {
			message = formatWithoutCause(message);
		}
		return message;
	}

	private String formatWithoutCause(String message) {
		return appendPeriod(format("%s", message));
	}

	private String formatWithCause(String message) {
		return appendPeriod(format("%s: %s", message, getCause().getMessage()));
	}

	private String appendPeriod(String message) {
		if (!message.endsWith(".")) {
			message = format("%s.", message);
		} else {
			message = message.replaceAll("\\.+$", ".");
		}
		return message;
	}
}
