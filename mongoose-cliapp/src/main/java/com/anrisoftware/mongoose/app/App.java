package com.anrisoftware.mongoose.app;

import java.io.Reader;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.parameter.Parameter;
import com.anrisoftware.mongoose.parameter.ParameterParser;
import com.anrisoftware.mongoose.parser.ScriptParser;
import com.anrisoftware.mongoose.parser.ScriptParserFactory;

/**
 * Parse the command line arguments and run the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class App {

	private final AppLogger log;

	private final ParameterParser parser;

	private final ScriptParserFactory scriptFactory;

	private final Environment environment;

	private final OpenReader openReader;

	private final FileName fileName;

	private Parameter parameter;

	private ScriptParser script;

	@Inject
	App(AppLogger logger, ParameterParser parser,
			ScriptParserFactory scriptFactory, Environment environment,
			OpenReader openReader, FileName fileName) {
		this.log = logger;
		this.parser = parser;
		this.scriptFactory = scriptFactory;
		this.environment = environment;
		this.openReader = openReader;
		this.fileName = fileName;
	}

	/**
	 * Parse the command line arguments and run the script.
	 * 
	 * @param args
	 *            the command line arguments.
	 * 
	 * @throws AppException
	 */
	public void start(String[] args) throws AppException {
		parseArgs(args);
		parameter = parser.getParameter();
		script = createScriptParser(parameter);
		script.setEnvironment(environment);
		callScript();
	}

	private void callScript() throws AppException {
		try {
			script.call();
		} catch (Exception e) {
			throw log.errorScript(e);
		}
	}

	private void parseArgs(String[] args) throws AppException {
		try {
			parser.parseParameter(args);
		} catch (CmdLineException e) {
			throw log.errorParseArgs(e, args);
		}
	}

	private ScriptParser createScriptParser(Parameter parameter)
			throws AppException {
		Reader source = openReader.open(parameter);
		String name = fileName.name(parameter);
		return scriptFactory.create(source, name);
	}

}
