package com.anrisoftware.mongoose.buildins.execbuildin;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.mongoose.api.exceptions.CommandException;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * If command run in an interactive terminal the parser will wrap the command in
 * a bash script to retain the return value of the command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandParser {

	private static final String REDIRECT = "sh -c \"%s 1> $outfile 2> $errfile; echo \\$? > $statusfile\"";

	private static final String SUFFIX = ".sh";

	private static final String PREFIX = "savereturnvalue";

	private static final String TERMINAL_COMMAND_PROPERTY = "terminal_command";

	private static final String BASH_COMMAND_PROPERTY = "bash_command";

	private static final URL SAVE_RETURN_VALUE_SCRIPT = CommandParser.class
			.getResource("savereturnvalue.sh");

	private static String saveReturnValueScript;

	private final CommandParserLogger log;

	private final String bashCommand;

	private String terminalCommand;

	private File scriptFile;

	private boolean useTerminal;

	private ExecBuildin buildin;

	@Inject
	CommandParser(CommandParserLogger logger,
			@Named("exec-properties") ContextProperties p) {
		this.log = logger;
		this.terminalCommand = p.getProperty(TERMINAL_COMMAND_PROPERTY);
		this.bashCommand = p.getProperty(BASH_COMMAND_PROPERTY);
		this.useTerminal = false;
		this.scriptFile = null;
	}

	public void setBuildin(ExecBuildin buildin) {
		this.buildin = buildin;
	}

	public void setTerminalCommand(String command) {
		this.terminalCommand = command;
	}

	public void setUseTerminal(boolean use) {
		this.useTerminal = use;
	}

	/**
	 * Returns the command line for the command.
	 * 
	 * @param command
	 *            the command {@link String}.
	 * 
	 * @return the {@link CommandLine}.
	 * 
	 * @throws CommandException
	 *             throws for errors creating the interactive terminal wrapper
	 *             script.
	 */
	public CommandLine parseCommand(String command) throws CommandException {
		String cmd = command;
		if (useTerminal) {
			scriptFile = createTempFile();
			cmd = StringUtils.replace(cmd, "\n", ";");
			int i = cmd.lastIndexOf(';');
			if (cmd.length() == i + 1) {
				cmd = StringUtils.substring(cmd, 0, -1);
			}
			cmd = format(REDIRECT, cmd);
			String terminal = terminalCommand.replace("{}", cmd);
			terminal = StringUtils.replace(terminal, ";;", ";");
			String script = loadSaveReturnValueScript();
			script = format(script, terminal);
			saveScript(scriptFile, script);
			cmd = format("%s %s", bashCommand, scriptFile.getAbsolutePath());

		}
		return CommandLine.parse(cmd);
	}

	private void saveScript(File file, String script) throws CommandException {
		try {
			FileUtils.write(file, script);
		} catch (IOException e) {
			throw log.errorSaveScript(buildin, e, file);
		}
	}

	private File createTempFile() throws CommandException {
		try {
			return File.createTempFile(PREFIX, SUFFIX);
		} catch (IOException e) {
			throw log.errorCreateTempFile(buildin, e);
		}
	}

	private String loadSaveReturnValueScript() throws CommandException {
		if (saveReturnValueScript == null) {
			try {
				saveReturnValueScript = IOUtils
						.toString(SAVE_RETURN_VALUE_SCRIPT);
			} catch (IOException e) {
				throw log.errorLoadScript(buildin, e, SAVE_RETURN_VALUE_SCRIPT);
			}
		}
		return saveReturnValueScript;
	}

	/**
	 * Remove created temporary files.
	 */
	public void cleanUp() {
		if (useTerminal) {
			scriptFile.delete();
			scriptFile = null;
		}
	}

}
