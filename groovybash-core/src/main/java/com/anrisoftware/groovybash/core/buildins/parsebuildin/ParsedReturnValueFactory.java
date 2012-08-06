package com.anrisoftware.groovybash.core.buildins.parsebuildin;

import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.groovybash.core.buildins.StandardStreams;
import com.google.inject.assistedinject.Assisted;

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
	 * @param bean
	 * @param parser
	 * 
	 * @return the {@link ParsedReturnValue}.
	 */
	ParsedReturnValue create(@Assisted StandardStreams streams,
			CmdLineParser parser, Object bean);
}
