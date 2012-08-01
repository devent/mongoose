package com.anrisoftware.groovybash.parameter

import java.nio.charset.Charset

import org.kohsuke.args4j.Option

class ParameterImpl implements Parameter {
	
	private File script
	
	private Charset charset
	
	ParameterImpl() {
		charset = Charset.defaultCharset()
	}

	@Option(name = "-input-script", aliases = ["-i"], required = true)
	void setScript(File file) {
		this.script = file
	}
	
	@Override
	File getScript() {
		script
	}
	
	@Option(name = "-charset", aliases = ["-C"], required = false)
	void setCharset(String charsetName) {
		this.charset = Charset.forName charsetName
	}
	
	@Override
	Charset getCharset() {
		charset
	}

}
