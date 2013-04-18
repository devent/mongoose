package com.anrisoftware.groovybash.application;

import javax.inject.Inject;

import com.anrisoftware.groovybash.parameter.ParameterParser;
import com.google.inject.Injector;

/**
 * Parse the command line arguments and run the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Application {

	private final ParameterParser parser;

	@Inject
	Application(ParameterParser parser) {
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
	public void start(String[] args, Injector injector) {
		parser.parseParameter(args, injector);
		parser.getBashParser().run();
	}
}
