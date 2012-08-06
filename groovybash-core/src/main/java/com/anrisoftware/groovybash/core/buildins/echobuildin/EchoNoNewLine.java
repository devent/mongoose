package com.anrisoftware.groovybash.core.buildins.echobuildin;


/**
 * The {@code echo} build-in command that will not output a newline after the
 * arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoNoNewLine extends EchoBuildin {

	public EchoNoNewLine(EchoBuildin parent) {
		super(parent);
	}

	@Override
	void outputText(String text) {
		getOutputStream().print(text);
		getOutputStream().flush();
	}
}
