package com.anrisoftware.mongoose.buildins.execbuildin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Logging messages for {@link CommandParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CommandParserLogger extends AbstractLogger {

	private static final String SAVE_SCRIPT_MESSAGE = "Error save script file '%s' for %s.";
	private static final String SAVE_SCRIPT = "Error save script file";
	private static final String RESOURCE = "resource";
	private static final String FILE = "file";
	private static final String CREATE_TMP_FILE_MESSAGE = "Error create temporary file for %s.";
	private static final String CREATE_TMP_FILE = "Error create temporary file";
	private static final String LOAD_SCRIPT_MESSAGE = "Error load script '%s' for %s.";
	private static final String LOAD_SCRIPT = "Error load script";
	private static final String BUILDIN = "buildin";

	/**
	 * Create logger for {@link CommandParser}.
	 */
	public CommandParserLogger() {
		super(CommandParser.class);
	}

	CommandException errorLoadScript(ExecBuildin buildin, IOException e, URL url) {
		return logException(
				new CommandException(LOAD_SCRIPT, e).add(BUILDIN,
						buildin).add(RESOURCE, url),
				LOAD_SCRIPT_MESSAGE, url, buildin.getTheName());
	}

	CommandException errorCreateTempFile(ExecBuildin buildin, IOException e) {
		return logException(
				new CommandException(CREATE_TMP_FILE, e).add(BUILDIN,
						buildin), CREATE_TMP_FILE_MESSAGE, buildin.getTheName());
	}

	CommandException errorSaveScript(ExecBuildin buildin, IOException e,
			File file) {
		return logException(
				new CommandException(SAVE_SCRIPT, e).add(BUILDIN,
						buildin).add(FILE, file), SAVE_SCRIPT_MESSAGE,
				file, buildin.getTheName());
	}
}
