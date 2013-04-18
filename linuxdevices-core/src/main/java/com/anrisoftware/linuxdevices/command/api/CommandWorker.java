package com.anrisoftware.linuxdevices.command.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Execute a command as a new process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface CommandWorker extends Callable<CommandWorker> {

	/**
	 * Starts the command as a new process and waits for its termination.
	 * 
	 * @throws IOException
	 *             if there is an error starting the process or read its
	 *             standard error or output; if the current thread is
	 *             interrupted while we wait for the termination of the process.
	 */
	@Override
	CommandWorker call() throws IOException;

	/**
	 * Sets the command that will be executed with parameter. Has no effect if
	 * the command was already executed by {@link #call()}.
	 * 
	 * @param command
	 *            the {@link List} with the command and parameter.
	 */
	void setCommandWithParameter(List<String> command);

	/**
	 * Returns the command that will be executed with parameter. The command and
	 * the parameter can be altered before invoke {@link #call()}.
	 * 
	 * @return the {@link List} with the first value is the command path and the
	 *         rest are the command parameter.
	 */
	List<String> getCommandWithParameter();

	/**
	 * Returns the command that will be executed without parameter.
	 * 
	 * @return the {@link String} command.
	 */
	String getCommand();

	/**
	 * Sets the executor service to create the thread to read the output from
	 * the process.
	 * 
	 * @param newExecutorService
	 *            the {@link ExecutorService}.
	 */
	void setExecutorService(ExecutorService newExecutorService);

	/**
	 * Sets the environment of the command. The environment is initialized to a
	 * copy of the current process environment and the same keys are overridden.
	 * <p>
	 * That way the environment can be set in a fluent API style:
	 * 
	 * <pre>
	 * commandWorkerFactory.create(command, streams).withEnvironment(env).call();
	 * </pre>
	 * 
	 * @param newEnvironment
	 *            the new environment {@link Map}.
	 * 
	 * @return this {@link CommandWorkerImpl}.
	 * 
	 * @see ProcessBuilder#environment()
	 */
	CommandWorker withEnvironment(Map<String, String> newEnvironment);

	/**
	 * Sets the environment of the command. The environment is initialized to a
	 * copy of the current process environment and the same keys are overridden.
	 * 
	 * @param newEnvironment
	 *            the new environment {@link Map}.
	 * 
	 * @see ProcessBuilder#environment()
	 */
	void setEnvironment(Map<String, String> newEnvironment);

	/**
	 * Redirect the error stream of the process to the output stream.
	 * <p>
	 * That way the environment can be set in a fluent API style:
	 * 
	 * <pre>
	 * commandWorkerFactory.create(command, streams).redirectErrorStream().call();
	 * </pre>
	 * 
	 * @return this {@link CommandWorkerImpl}.
	 */
	CommandWorker redirectErrorStream();

	/**
	 * Sets if the error stream of the process should be redirected to the
	 * output stream.
	 * 
	 * @param redirectErrorStream
	 *            {@code true} if the error stream of the process should be
	 *            redirected to the output stream, {@code false} if not.
	 * 
	 * @see ProcessBuilder#redirectErrorStream(boolean)
	 */
	void setRedirectErrorStream(boolean redirectErrorStream);

	/**
	 * Sets if the input stream of the process should be redirected to the set
	 * output stream in {@link #setStandardStreams(StandardStreams)}.
	 * <p>
	 * That way the environment can be set in a fluent API style:
	 * 
	 * <pre>
	 * commandWorkerFactory.create(command, streams).redirectInputStream().call();
	 * </pre>
	 * 
	 * @param redirect
	 *            {@code true} if the input stream of the process should be
	 *            redirected to the set output stream, {@code false} if not.
	 * 
	 * @see #setStandardStreams(StandardStreams)
	 * 
	 * @return this {@link CommandWorkerImpl}.
	 */
	CommandWorker redirectInputStream();

	/**
	 * Sets if the input stream of the process should be redirected to the set
	 * output stream in {@link #setStandardStreams(StandardStreams)}.
	 * 
	 * @param redirect
	 *            {@code true} if the input stream of the process should be
	 *            redirected to the set output stream, {@code false} if not.
	 * 
	 * @see #setStandardStreams(StandardStreams)
	 */
	void setRedirectInputStream(boolean redirect);

	/**
	 * Sets the standard streams for the command.
	 * <p>
	 * That way the environment can be set in a fluent API style:
	 * 
	 * <pre>
	 * commandWorkerFactory.create(command, streams).withStandardStreams(streams)
	 * 		.call();
	 * </pre>
	 * 
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard output,
	 *            error and input streams.
	 * 
	 * @return this {@link CommandWorkerImpl}.
	 */
	CommandWorker withStandardStreams(StandardStreams streams);

	/**
	 * Sets the standard streams for the command.
	 * 
	 * @param streams
	 *            the {@link StandardStreams} containing the standard output,
	 *            error and input streams.
	 */
	void setStandardStreams(StandardStreams streams);

	/**
	 * Returns the stream that is connected to the process input.
	 * 
	 * @return the {@link OutputStream}.
	 */
	OutputStream getProcessInput();

	/**
	 * Returns the stream that is connected to the process output.
	 * 
	 * @return the {@link InputStream}.
	 */
	InputStream getProcessOutput();

	/**
	 * Returns the stream that is connected to the process error.
	 * 
	 * @return the {@link InputStream}.
	 */
	InputStream getProcessError();

	/**
	 * Returns the exit code from the command after the command finish.
	 * 
	 * @return the exit code.
	 * 
	 * @throws IllegalStateException
	 *             if the command was not yet executed with {@link #call()}.
	 * 
	 * @see Process#waitFor()
	 */
	int getExitCode();

}