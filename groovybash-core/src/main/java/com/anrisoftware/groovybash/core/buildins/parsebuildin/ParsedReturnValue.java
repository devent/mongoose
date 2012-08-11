package com.anrisoftware.groovybash.core.buildins.parsebuildin;

import static com.google.common.base.Preconditions.checkState;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns the parsed parameters.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ParsedReturnValue extends GroovyObjectSupport implements ReturnValue {

	private final CmdLineParser parser;

	private final Object bean;

	private final StandardStreams streams;

	private final boolean valid;

	/**
	 * Sets the standard streams to print the examples and usages, the parser,
	 * the Java bean class and whether or not the arguments are parsed
	 * correctly.
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
	 */
	@Inject
	public ParsedReturnValue(@Assisted StandardStreams streams,
			@Assisted CmdLineParser parser, @Assisted Object bean,
			@Assisted boolean valid) {
		this.streams = streams;
		this.parser = parser;
		this.bean = bean;
		this.valid = valid;
	}

	/**
	 * Returns an example of the command line arguments.
	 * 
	 * @return an example of the command line arguments.
	 * 
	 * @since 0.2
	 */
	public String getExample() {
		return parser.printExample(ExampleMode.ALL);
	}

	/**
	 * Returns an usage example in a single line of the command line arguments.
	 * 
	 * @return an example of the usage in a single line.
	 * 
	 * @since 0.2
	 */
	public String getSingleLineUsage() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		parser.printSingleLineUsage(out);
		return out.toString();
	}

	/**
	 * Returns an usage example of the command line arguments.
	 * 
	 * @return an example of the usage.
	 * 
	 * @since 0.2
	 */
	public String getUsage() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		parser.printUsage(out);
		return out.toString();
	}

	/**
	 * Print an example of the command line arguments in the standard output.
	 */
	public void printExample() {
		String example = parser.printExample(ExampleMode.ALL);
		streams.getOutputStream().print(example);
	}

	/**
	 * Print an example of the usage of the command line arguments in one line
	 * in the standard output.
	 */
	public void printSingleLineUsage() {
		parser.printSingleLineUsage(streams.getOutputStream());
	}

	/**
	 * Print the usage of the command line arguments in one line in the standard
	 * output.
	 */
	public void printUsage() {
		parser.printUsage(streams.getOutputStream());
	}

	/**
	 * Returns whether or not the arguments are parsed and the bean class
	 * contains the valid parameter.
	 * <p>
	 * An {@link IllegalStateException} will be thrown for trying to access the
	 * properties of the bean class if the arguments are not parsed correctly.
	 * 
	 * @return {@code true} if the arguments are parsed and the bean class
	 *         contains the valid parameter, {@code false} otherwise.
	 */
	public boolean getIsValid() {
		return valid;
	}

	public Object valid(Closure<?> closure) {
		if (valid) {
			closure.call(parser);
			return this;
		}
		return this;
	}

	public Object notValid(Closure<?> closure) {
		if (!valid) {
			return closure.call(parser);
		}
		return this;
	}

	/**
	 * Not found properties are delegated to the parameter bean object.
	 * 
	 * @throws IllegalStateException
	 *             is thrown for trying to access the properties of the bean
	 *             class if the arguments are not parsed correctly.
	 * 
	 * @see #getIsValid()
	 */
	@Override
	public Object getProperty(String name) {
		if (getMetaClass().hasProperty(this, name) != null) {
			return super.getProperty(name);
		}
		checkState(valid,
				"The command line arguments were not parsed correctly.");
		return InvokerHelper.getProperty(bean, name);
	}

}
