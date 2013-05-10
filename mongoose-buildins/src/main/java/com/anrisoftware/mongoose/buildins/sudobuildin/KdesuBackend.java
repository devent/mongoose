package com.anrisoftware.mongoose.buildins.sudobuildin;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Uses the command {@code kdesu(1)} to execute commands with root privileges.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class KdesuBackend implements Backend {

	private static final String KDESU_COMMAND_DEFAULT = "/usr/bin/kdesu";

	private static final String KDESU_COMMAND_PROPERTY = "kdesu_command";

	private final Command command;

	private final String sudoCommand;

	@Inject
	KdesuBackend(CommandLoader loader,
			@Named("sudo-properties") ContextProperties p) {
		this.command = loader.loadCommand("exec");
		this.sudoCommand = p.getProperty(KDESU_COMMAND_PROPERTY,
				KDESU_COMMAND_DEFAULT);
	}

	@Override
	public Command getBackendCommand(Map<String, Object> args,
			List<Object> unnamed) throws Exception {
		insertSudo(unnamed);
		command.args(args, unnamed.toArray());
		return command;
	}

	private void insertSudo(List<Object> unnamed) {
		String cmd = unnamed.get(0).toString();
		unnamed.set(0, format("%s -t -c %s", sudoCommand, cmd));
	}

}
