package com.anrisoftware.mongoos.command;

import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.Environment;

/**
 * Sets the standard streams of the command and implements the stream
 * redirection operators.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractCommand implements Command {

	/**
	 * Key for the unnamed arguments.
	 */
	public static final String UNNAMED_KEY = "unnamed";

	private final VetoableChangeSupport vetoable;

	private AbstractCommandLogger log;

	private StandardStreams streams;

	private Map<String, Object> args;

	private Environment environment;

	protected AbstractCommand() {
		this.vetoable = new VetoableChangeSupport(this);
	}

	@Inject
	void setAbstractCommandLogger(AbstractCommandLogger logger) {
		this.log = logger;
	}

	public void setStreams(StandardStreams streams) {
		this.streams = streams;
	}

	public StandardStreams getStreams() {
		return streams;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public Command call(Object... args) throws Exception {
		args(args);
		return call();
	}

	@Override
	public Command call(Map<String, Object> named, Object... args)
			throws Exception {
		args(named, args);
		return call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setArgs(Object args) throws Exception {
		log.checkArgs(this, args);
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		if (args instanceof Map) {
			newValue.putAll((Map<String, Object>) args);
		} else {
			newValue.put(UNNAMED_KEY, args);
		}
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		log.argumentsSet(this, args);
	}

	private Map<String, Object> createMap(int size) {
		return new LinkedHashMap<String, Object>(size);
	}

	@Override
	public Command args(Object... args) throws Exception {
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		newValue.put(UNNAMED_KEY, Arrays.asList(args));
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		log.argumentsSet(this, args);
		return this;
	}

	@Override
	public Command args(Map<String, Object> named, Object... args)
			throws Exception {
		log.checkArgs(this, named);
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		newValue.putAll(named);
		newValue.put(UNNAMED_KEY, Arrays.asList(args));
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		log.argumentsSet(this, args);
		return this;
	}

	@Override
	public Map<String, Object> getArgs() {
		return args;
	}

	/**
	 * Returns the argument values as list.
	 * 
	 * @return the argument values {@link List}.
	 */
	public List<Object> getArgsList() {
		return new ArrayList<Object>(args.values());
	}

	@Override
	public void setInputStream(OutputStream stream) {
		streams.setInputStream(stream);
	}

	@Override
	public void setInput(Object source) throws Exception {
		log.checkSource(this, source);
		Object newValue;
		InputStream stream;
		if (source instanceof InputStream) {
			newValue = source;
			stream = (InputStream) source;
		} else if (source instanceof File) {
			newValue = source;
			stream = createInputStream((File) source);
		} else {
			newValue = new File(source.toString());
			stream = createInputStream((File) newValue);
		}
		vetoable.fireVetoableChange(Command.INPUT_SOURCE_PROPERTY, null,
				newValue);
		streams.setInputSource(stream);
		log.sourceSet(this, source);
	}

	private InputStream createInputStream(File file)
			throws FileNotFoundException {
		return new FileInputStream(file);
	}

	@Override
	public Command input(Object source) throws Exception {
		setInput(source);
		return this;
	}

	@Override
	public OutputStream getInputStream() {
		return streams.getInputStream();
	}

	@Override
	public void setOutputStream(InputStream stream) {
		streams.setOutputStream(stream);
	}

	@Override
	public InputStream getOutputStream() {
		return streams.getOutputStream();
	}

	@Override
	public void setOutput(Object target) throws Exception {
		setOutput(target);
	}

	@Override
	public void setOutput(Object target, boolean append) throws Exception {
		setOutput(target, append);
	}

	@Override
	public void setOutput(int descriptor, Object target, boolean append)
			throws Exception {
		log.checkTarget(this, target);
		Object newValue;
		OutputStream stream;
		if (target instanceof InputStream) {
			newValue = target;
			stream = (OutputStream) target;
		} else if (target instanceof File) {
			newValue = target;
			stream = createOutputStream((File) target, append);
		} else {
			newValue = new File(target.toString());
			stream = createOutputStream((File) newValue, append);
		}
		vetoable.fireVetoableChange(Command.OUTPUT_TARGET_PROPERTY, null,
				newValue);
		streams.setOutputTarget(descriptor, stream);
		log.outputTargetSet(this, target);
	}

	private OutputStream createOutputStream(File file, boolean append)
			throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	@Override
	public Command output(Object target) throws Exception {
		setOutput(target);
		return this;
	}

	@Override
	public void setErrorStream(InputStream stream) {
		streams.setErrorStream(stream);
	}

	@Override
	public InputStream getErrorStream() {
		return streams.getErrorStream();
	}

	@Override
	public void setError(Object target) throws Exception {
		setError(target);
	}

	@Override
	public void setError(Object target, boolean append) throws Exception {
		log.checkTarget(this, target);
		Object newValue;
		OutputStream stream;
		if (target instanceof InputStream) {
			newValue = target;
			stream = (OutputStream) target;
		} else if (target instanceof File) {
			newValue = target;
			stream = createOutputStream((File) target, append);
		} else {
			newValue = new File(target.toString());
			stream = createOutputStream((File) newValue, append);
		}
		vetoable.fireVetoableChange(Command.ERROR_TARGET_PROPERTY, null,
				newValue);
		streams.setErrorTarget(stream);
		log.errorTargetSet(this, target);
	}

	@Override
	public Command error(Object target) throws Exception {
		setError(target);
		return this;
	}

	/**
	 * Operator {@code |}. The \Operator{|} creates a pipe between the left
	 * command and the right command.
	 * 
	 * @param rhs
	 *            the right hand side {@link Command}.
	 * 
	 * @return the right hand side {@link Command}.
	 * 
	 * @throws Exception
	 *             if there was an error set the input of the command; if the
	 *             left command returns with an error.
	 */
	public Command or(Command rhs) throws Exception {
		rhs.setInput(getOutputStream());
		environment.executeCommandAndWait(this);
		return rhs;
	}

	public Command leftShift(Object rhs) {
		return this;
	}

	public Command rightShift(Object rhs) {
		return this;
	}

	@Override
	public void addVetoableChangeListener(VetoableChangeListener listener) {
		vetoable.addVetoableChangeListener(listener);
	}

	@Override
	public void removeVetoableChangeListener(VetoableChangeListener listener) {
		vetoable.removeVetoableChangeListener(listener);
	}

	@Override
	public void addVetoableChangeListener(String propertyName,
			VetoableChangeListener listener) {
		vetoable.addVetoableChangeListener(propertyName, listener);
	}

	@Override
	public void removeVetoableChangeListener(String propertyName,
			VetoableChangeListener listener) {
		vetoable.removeVetoableChangeListener(propertyName, listener);
	}
}
