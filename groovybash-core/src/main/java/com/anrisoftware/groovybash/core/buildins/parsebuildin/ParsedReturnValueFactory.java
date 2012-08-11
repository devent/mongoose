package com.anrisoftware.groovybash.core.buildins.parsebuildin;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.groovybash.core.buildins.StandardStreams;

/**
 * Factory to create a new return value for parsed command line arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface ParsedReturnValueFactory {

	/**
	 * Creates a new return value for parsed command line arguments.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard input,
	 *            output and error streams. Needed to print the usage and
	 *            examples to.
	 * 
	 * @param bean
	 *            the Java bean class annotated with {@link Option} and
	 *            {@link Argument}.
	 * 
	 * @param parser
	 *            the {@link CmdLineParser} that print the usage and examples.
	 * 
	 * @param valid
	 *            whether or not the arguments are parsed and the bean class
	 *            contains the valid parameter.
	 * 
	 * @return the {@link ParsedReturnValue}.
	 */
	ParsedReturnValue create(StandardStreams streams, CmdLineParser parser,
			Object bean, boolean valid);
}
