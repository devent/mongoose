package com.anrisoftware.mongoose.application;

import javax.inject.Inject;

import com.anrisoftware.mongoose.parameter.ParameterParser;
import com.google.inject.Injector;

/**
 * Parse the command line arguments and run the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class App {

	private final ParameterParser parser;

	@Inject
	App(ParameterParser parser) {
		this.parser = parser;
	}

	/**
	 * Parse the command line arguments and run the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 * 
	 * @param injector
	 *            the {@link Injector}.
	 */
	public void start(String[] args) {
		parser.parseParameter(args);
		parser.getBashParser().run();
	}
}
