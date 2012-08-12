/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.buildins.parsebuildin;

import static com.google.common.base.Preconditions.checkState;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import com.anrisoftware.groovybash.core.ReturnValue;
import com.google.inject.assistedinject.Assisted;

/**
 * Returns the parsed parameters.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParsedReturnValue extends GroovyObjectSupport implements ReturnValue {

	private final CmdLineParser parser;

	private final Object bean;

	private final PrintStream output;

	private final boolean valid;

	/**
	 * Sets the standard streams to print the examples and usages, the parser,
	 * the Java bean class and whether or not the arguments are parsed
	 * correctly.
	 * 
	 * @param output
	 *            the {@link PrintStreams} to print the usage and examples to.
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
	public ParsedReturnValue(@Assisted PrintStream output,
			@Assisted CmdLineParser parser, @Assisted Object bean,
			@Assisted boolean valid) {
		this.output = output;
		this.parser = parser;
		this.bean = bean;
		this.valid = valid;
	}

	/**
	 * Returns an example of the command line arguments.
	 * 
	 * @return an example of the command line arguments.
	 */
	public String getExample() {
		return parser.printExample(ExampleMode.ALL);
	}

	/**
	 * Returns an usage example in a single line of the command line arguments.
	 * 
	 * @return an example of the usage in a single line.
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
		output.print(example);
	}

	/**
	 * Print an example of the usage of the command line arguments in one line
	 * in the standard output.
	 */
	public void printSingleLineUsage() {
		parser.printSingleLineUsage(output);
	}

	/**
	 * Print the usage of the command line arguments in one line in the standard
	 * output.
	 */
	public void printUsage() {
		parser.printUsage(output);
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

	/**
	 * Equals only if the object is a boolean and the valid equals to the
	 * expected boolean value, or if of the same class and valid and the bean
	 * are equals.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Boolean) {
			Boolean expecting = (Boolean) obj;
			return valid == expecting;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ParsedReturnValue rhs = (ParsedReturnValue) obj;
		return new EqualsBuilder().append(valid, rhs.valid)
				.append(bean, rhs.bean).isEquals();

	}
}
