package com.anrisoftware.groovybash.core.buildins.parsebuildin;

import groovy.lang.GroovyObjectSupport;

import javax.inject.Inject;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import com.anrisoftware.groovybash.core.api.ReturnValue;
import com.anrisoftware.groovybash.core.buildins.StandardStreams;
import com.google.inject.assistedinject.Assisted;

class ParsedReturnValue extends GroovyObjectSupport implements ReturnValue {

	private final CmdLineParser parser;

	private final Object bean;

	private final StandardStreams streams;

	@Inject
	public ParsedReturnValue(@Assisted StandardStreams streams,
			@Assisted CmdLineParser parser, @Assisted Object bean) {
		this.streams = streams;
		this.parser = parser;
		this.bean = bean;
	}

	public void printExample() {
		String example = parser.printExample(ExampleMode.ALL);
		streams.getOutputStream().print(example);
	}

	public void printSingleLineUsage() {
		parser.printSingleLineUsage(streams.getOutputStream());
	}

	public void printUsage() {
		parser.printUsage(streams.getOutputStream());
	}

	@Override
	public Object getProperty(String name) {
		if (getMetaClass().hasProperty(this, name) != null) {
			return super.getProperty(name);
		}
		return InvokerHelper.getProperty(bean, name);
	}

}
