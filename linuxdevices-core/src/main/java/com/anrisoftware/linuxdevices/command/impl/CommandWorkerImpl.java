package com.anrisoftware.linuxdevices.command.impl;

import static org.apache.commons.lang3.Validate.validState;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import com.anrisoftware.linuxdevices.command.api.CommandWorker;
import com.anrisoftware.linuxdevices.command.api.StandardStreams;
import com.csvreader.CsvReader;
import com.google.inject.assistedinject.Assisted;

/**
 * Execute a command as a new process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CommandWorkerImpl implements CommandWorker {

	private List<String> command;

	private final OutputTaskFactory outputWorkerFactory;

	private final InputTaskFactory inputWorkerFactory;

	private boolean redirectErrorStream;

	private PrintStream output;

	private PrintStream error;

	private InputStream input;

	private CommandWorkerLogger log;

	private ExecutorService executorService;

	private Process process;

	private Integer exitCode;

	private Map<? extends String, ? extends String> environment;

	private File workingDirectory;

	private boolean redirectInputStream;

	/**
	 * Sets the command to execute as a new process.
	 * 
	 * @param command
	 *            the command.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard output,
	 *            error and input streams.
	 * 
	 */
	@Inject
	protected CommandWorkerImpl(OutputTaskFactory outputTaskFactory,
			InputTaskFactory inputWorkerFactory, @Assisted String command,
			@Assisted StandardStreams streams) {
		this.outputWorkerFactory = outputTaskFactory;
		this.inputWorkerFactory = inputWorkerFactory;
		this.command = getCommand(command);
		this.redirectErrorStream = false;
		this.redirectInputStream = false;
		this.output = new PrintStream(streams.outputStream);
		this.error = new PrintStream(streams.errorStream);
		this.input = new BufferedInputStream(streams.inputStream);
		this.executorService = Executors.newFixedThreadPool(3);
		this.environment = new HashMap<String, String>();
	}

	private List<String> getCommand(String string) {
		Reader reader = new StringReader(string);
		CsvReader csv = new CsvReader(reader, ' ');
		try {
			csv.readRecord();
			return new ArrayList<String>(Arrays.asList(csv.getValues()));
		} catch (IOException e) {
			throw log.errorReadCommandString(e);
		}
	}

	@Inject
	void setCommandWorkerLogger(CommandWorkerLogger logger) {
		this.log = logger;
	}

	@Override
	public CommandWorker call() throws IOException {
		Process process = startProcess();
		Future<?> outputTask;
		Future<?> errorTask;
		outputTask = submitOutputWorker(process.getInputStream(), output);
		errorTask = submitOutputWorker(process.getErrorStream(), error);
		if (redirectInputStream) {
			Future<?> inputTask;
			inputTask = submitInputWorker(process.getOutputStream(), input);
			waitInputForTask(inputTask);
		}
		waitForOutputTask(outputTask);
		waitForOutputTask(errorTask);
		waitForProcess(process);
		log.commandFinish(command, exitCode);
		this.process = process;
		return this;
	}

	private void waitForProcess(Process process) throws IOException {
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			throw log.interruptedErrorProcess(getCommand(), e);
		}
	}

	private Process startProcess() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectInput(Redirect.INHERIT);
		Map<String, String> parentEnvironment = builder.environment();
		parentEnvironment.putAll(environment);
		builder.directory(workingDirectory);
		builder.redirectErrorStream(redirectErrorStream);
		log.startingCommand(command);
		Process process = builder.start();
		return process;
	}

	private Future<?> submitInputWorker(OutputStream stream,
			InputStream inputStream) {
		InputWorker worker = inputWorkerFactory.create(stream, inputStream);
		Future<?> task = executorService.submit(worker, worker);
		return task;
	}

	private Future<?> submitOutputWorker(InputStream stream,
			PrintStream outputStream) {
		OutputWorker worker = outputWorkerFactory.create(stream, outputStream);
		Future<?> task = executorService.submit(worker, worker);
		return task;
	}

	private void waitForOutputTask(Future<?> task) throws IOException {
		try {
			OutputWorker worker = (OutputWorker) task.get();
			if (worker.getIoException() != null) {
				throw worker.getIoException();
			}
			if (worker.getInterruptedException() != null) {
				throw worker.getInterruptedException();
			}
		} catch (ExecutionException e) {
			throw log.unexpectedErrorOutputTask(getCommand(), e);
		} catch (InterruptedException e) {
			throw log.interruptedErrorOutputTask(getCommand(), e);
		}
	}

	private void waitInputForTask(Future<?> task) throws IOException {
		try {
			InputWorker worker = (InputWorker) task.get();
			if (worker.getIoException() != null) {
				throw worker.getIoException();
			}
			if (worker.getInterruptedException() != null) {
				throw worker.getInterruptedException();
			}
		} catch (ExecutionException e) {
			throw log.unexpectedErrorOutputTask(getCommand(), e);
		} catch (InterruptedException e) {
			throw log.interruptedErrorOutputTask(getCommand(), e);
		}
	}

	@Override
	public void setCommandWithParameter(List<String> newCommand) {
		command = newCommand;
	}

	@Override
	public List<String> getCommandWithParameter() {
		return command;
	}

	@Override
	public String getCommand() {
		return command.get(0);
	}

	@Override
	public void setExecutorService(ExecutorService newExecutorService) {
		if (executorService != null) {
			executorService.shutdown();
		}
		executorService = newExecutorService;
	}

	@Override
	public CommandWorker withEnvironment(Map<String, String> newEnvironment) {
		setEnvironment(newEnvironment);
		return this;
	}

	@Override
	public void setEnvironment(Map<String, String> newEnvironment) {
		environment = newEnvironment;
	}

	@Override
	public CommandWorker redirectErrorStream() {
		setRedirectErrorStream(true);
		return this;
	}

	@Override
	public void setRedirectErrorStream(boolean redirectErrorStream) {
		this.redirectErrorStream = redirectErrorStream;
	}

	@Override
	public CommandWorker redirectInputStream() {
		setRedirectErrorStream(true);
		return this;
	}

	@Override
	public void setRedirectInputStream(boolean redirect) {
		this.redirectInputStream = redirect;
	}

	@Override
	public CommandWorker withStandardStreams(StandardStreams streams) {
		setStandardStreams(streams);
		return this;
	}

	@Override
	public void setStandardStreams(StandardStreams streams) {
		output = new PrintStream(streams.outputStream);
		error = new PrintStream(streams.errorStream);
		input = new BufferedInputStream(streams.inputStream);
	}

	@Override
	public OutputStream getProcessInput() {
		return process.getOutputStream();
	}

	@Override
	public InputStream getProcessOutput() {
		return process.getInputStream();
	}

	@Override
	public InputStream getProcessError() {
		return process.getErrorStream();
	}

	@Override
	public int getExitCode() {
		validState(exitCode != null, "The command was not yet executed.");
		return exitCode;
	}

}
