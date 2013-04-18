package com.anrisoftware.linuxdevices.command.impl;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.anrisoftware.linuxdevices.command.api.CheckIsRootUser;
import com.anrisoftware.linuxdevices.command.api.StandardStreams;
import com.anrisoftware.linuxdevices.command.api.SuCommandWorker;
import com.google.inject.assistedinject.Assisted;

/**
 * Execute a command as a new process in the context of the root user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class SuCommandWorkerImpl extends CommandWorkerImpl implements SuCommandWorker {

	private final SuCommandWorkerLogger log;

	private List<String> sudoCommand;

	private CheckIsRootUser isRootUser;

	/**
	 * Sets the command to execute as a new process in the context of the roor
	 * user.
	 * 
	 * @param isRootUser
	 *            the {@link CheckIsRootUser} to check if the current user is
	 *            the root user.
	 * 
	 * @param command
	 *            the command.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard output,
	 *            error and input streams.
	 */
	@Inject
	SuCommandWorkerImpl(OutputTaskFactory outputTaskFactory,
			InputTaskFactory inputWorkerFactory, SuCommandWorkerLogger logger,
			CheckIsRootUser isRootUser, @Assisted String command,
			@Assisted StandardStreams streams) {
		super(outputTaskFactory, inputWorkerFactory, command, streams);
		this.log = logger;
		this.isRootUser = isRootUser;
		this.sudoCommand = splitSudoCommand("/bin/sudo {}");
	}

	@Override
	public CommandWorkerImpl call() throws IOException {
		if (!isRootUser.isRoot()) {
			List<String> newCommand = constructSudoCommand();
			setCommandWithParameter(newCommand);
		}
		super.call();
		return this;
	}

	private List<String> constructSudoCommand() {
		List<String> oldCommand = getCommandWithParameter();
		List<String> newCommand = new ArrayList<String>();
		for (String sudopart : sudoCommand) {
			if (sudopart.equals("{}")) {
				newCommand.addAll(oldCommand);
				continue;
			}
			newCommand.add(sudopart);
		}
		log.userIsNotRoot(getCommand(), getSudoCommand());
		return newCommand;
	}

	@Override
	public void setSudoCommand(String sudoCommand) {
		this.sudoCommand = splitSudoCommand(sudoCommand);
	}

	private List<String> splitSudoCommand(String string) {
		return new ArrayList<String>(asList(split(string, ' ')));
	}

	@Override
	public String getSudoCommand() {
		return sudoCommand.get(0);
	}

	@Override
	public void setCheckIsRootUser(CheckIsRootUser isRootUser) {
		this.isRootUser = isRootUser;
	}
}
