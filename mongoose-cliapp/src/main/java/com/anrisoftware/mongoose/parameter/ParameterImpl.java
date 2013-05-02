package com.anrisoftware.mongoose.parameter;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import org.kohsuke.args4j.Option;

class ParameterImpl implements Parameter {

	private String[] args;

	private Charset charset;

	private File scriptFile;

	private URL scriptResource;

	ParameterImpl() {
		this.charset = Charset.defaultCharset();
	}

	@Option(name = "-file")
	public void setScriptFile(File file) {
		this.scriptFile = file;
	}

	@Override
	public File getScriptFile() {
		return scriptFile;
	}

	@Option(name = "-resource")
	public void setScriptResource(URL url) {
		this.scriptResource = url;
	}

	@Override
	public URL getScriptResource() {
		return scriptResource;
	}

	@Option(name = "-charset")
	public void setCharset(String charsetName) {
		this.charset = Charset.forName(charsetName);
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public String[] getArgs() {
		return args;
	}
}
