package com.anrisoftware.groovybash.buildins.echobuildin;

import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;

class FromInput extends EchoBuildin {

	private final EchoBuildin delegateBuildin;

	public FromInput(EchoBuildin parent) {
		super(parent);
		this.delegateBuildin = parent;
	}

	@Override
	void outputText(String text) throws IOException {
		delegateBuildin.outputText(text);
	}

	@Override
	String getOutput() throws IOException {
		return CharStreams.toString(new InputStreamReader(getInputStream()));
	}
}
