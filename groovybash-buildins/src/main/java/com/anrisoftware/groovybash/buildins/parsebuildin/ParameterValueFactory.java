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

import java.io.PrintStream;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Factory to create a new return value for parsed command line arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
interface ParameterValueFactory {

	/**
	 * Creates a new return value for parsed command line arguments.
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
	 * 
	 * @return the {@link ParameterValue}.
	 */
	ParameterValue create(PrintStream output, CmdLineParser parser,
			Object bean, boolean valid);
}
