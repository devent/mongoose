package com.anrisoftware.groovybash.core;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.groovybash.core.api.BuildinPlugin;
import com.google.inject.assistedinject.Assisted;

public class BashParser {

	private final List<BuildinPlugin> buildinPlugins;

	private final Script script;

	@Inject
	BashParser(List<BuildinPlugin> buildinPlugins, @Assisted String script) {
		this.buildinPlugins = buildinPlugins;
		this.script = new GroovyShell().parse(script);
		setup();
	}

	private void setup() {
	}

}
