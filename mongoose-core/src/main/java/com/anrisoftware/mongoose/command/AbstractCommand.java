package com.anrisoftware.mongoose.command;

import static com.anrisoftware.mongoose.command.StandardStreams.STANDRD_OUTPUT_DESCRIPTOR;
import static java.lang.String.format;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.commans.ListenableFuture;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.api.exceptions.CommandException;

/**
 * Sets the standard streams of the command and implements the stream
 * redirection operators.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractCommand implements Command {

	private static final String LISTENER_KEY = "listener";

	private static final String LISTENERS_KEY = "listeners";

	/**
	 * Key for the unnamed arguments.
	 */
	public static final String UNNAMED_KEY = "unnamed";

	private final VetoableChangeSupport vetoable;

	private AbstractCommandLogger log;

	private StandardStreams streams;

	private Map<String, Object> args;

	private Environment environment;

	private int pipeBufferSize;

	protected AbstractCommand() {
		this.vetoable = new VetoableChangeSupport(this);
		this.pipeBufferSize = 1024;
		this.args = createMap(1);
		args.put(UNNAMED_KEY, new ArrayList<Object>(0));
	}

	@Inject
	void setAbstractCommandLogger(AbstractCommandLogger logger) {
		this.log = logger;
	}

	@Inject
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
	public Environment getTheEnvironment() {
		return environment;
	}

	@Override
	public Command call() throws Exception {
		synchronized (streams) {
			doCall();
			streams.close();
			return this;
		}
	}

	@Override
	public Command call(Object... args) throws Exception {
		args(args);
		synchronized (streams) {
			doCall();
			streams.close();
			return this;
		}
	}

	@Override
	public synchronized Command call(Map<String, Object> named, Object... args)
			throws Exception {
		args(named, args);
		synchronized (streams) {
			doCall();
			streams.close();
			return this;
		}
	}

	/**
	 * Execute the command after the parent are set.
	 * 
	 * @throws Exception
	 *             if there was an error executing the command.
	 */
	protected abstract void doCall() throws Exception;

	@Override
	public void setArgs(Object args) throws Exception {
		setArgs(null, args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setArgs(Object arg, Object args) throws Exception {
		log.checkArgs(this, args);
		Object[] commandArgs = parseArgs(asList(args));
		insertAdditional(arg, (List<Object>) commandArgs[1]);
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		newValue.putAll((Map<String, Object>) commandArgs[0]);
		newValue.put(UNNAMED_KEY, commandArgs[1]);
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		argumentsSet(getArgs(), getUnnamedArgs());
		log.argumentsSet(this, args);
	}

	private void insertAdditional(Object arg, List<Object> unnamed) {
		if (arg != null && unnamed.size() > 0) {
			unnamed.set(0, format("%s %s", arg, unnamed.get(0)));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object> asList(Object obj) {
		if (obj instanceof List) {
			return (List<Object>) obj;
		} else if (obj.getClass().isArray()) {
			return Arrays.asList((Object[]) obj);
		} else {
			List<Object> list = new ArrayList<Object>();
			list.add(obj);
			return list;
		}
	}

	private Object[] parseArgs(List<Object> list) {
		Map<String, Object> named = new HashMap<String, Object>();
		List<Object> unnamed = new ArrayList<Object>(list.size());
		int index = 0;
		if (list.size() > 0) {
			if (list.get(0) instanceof Map) {
				putNamed(named, (Map<?, ?>) list.get(0));
				index++;
			}
			for (int i = index; i < list.size(); i++) {
				unnamed.add(list.get(i));
			}
		}
		return new Object[] { named, unnamed };
	}

	private void putNamed(Map<String, Object> named, Map<?, ?> map) {
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			named.put(entry.getKey().toString(), entry.getValue());
		}
	}

	@Override
	public Command args(Object... args) throws Exception {
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		newValue.put(UNNAMED_KEY, new ArrayList<Object>(Arrays.asList(args)));
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		log.argumentsSet(this, args);
		argumentsSet(getArgs(), getUnnamedArgs());
		return this;
	}

	@Override
	public Command args(Map<String, Object> named, Object... args)
			throws Exception {
		return setupArgs(named, args);
	}

	private Command setupArgs(Map<String, Object> named, Object[] args)
			throws PropertyVetoException, Exception {
		log.checkArgs(this, named);
		Map<String, Object> oldValue = this.args;
		Map<String, Object> newValue = createMap(10);
		newValue.putAll(named);
		newValue.put(UNNAMED_KEY, new ArrayList<Object>(Arrays.asList(args)));
		vetoable.fireVetoableChange(ARGUMENTS_PROPERTY, oldValue, newValue);
		this.args = newValue;
		log.argumentsSet(this, args);
		argumentsSet(getArgs(), getUnnamedArgs());
		return this;
	}

	private Map<String, Object> createMap(int size) {
		return new LinkedHashMap<String, Object>(size);
	}

	/**
	 * Called after the arguments are set.
	 * 
	 * @param args
	 *            the {@link Map} with the arguments.
	 * 
	 * @param unnamedArgs
	 *            the {@link List} with the unnamed arguments.
	 * 
	 * @throws Exception
	 *             if some errors are encountered.
	 */
	protected void argumentsSet(Map<String, Object> args,
			List<Object> unnamedArgs) throws Exception {
	}

	@Override
	public ListenableFuture<Command> background(Map<String, Object> named,
			Object... args) throws Exception {
		PropertyChangeListener[] l = parseBackgroundListener(named);
		args(named, args);
		return l == null ? environment.executeCommand(this) : environment
				.executeCommand(this, l);
	}

	@SuppressWarnings("unchecked")
	private PropertyChangeListener[] parseBackgroundListener(
			Map<String, Object> named) {
		List<PropertyChangeListener> l = new ArrayList<PropertyChangeListener>();
		if (named.containsKey(LISTENER_KEY)) {
			l.add((PropertyChangeListener) named.get(LISTENER_KEY));
			named.remove(LISTENER_KEY);
		}
		if (named.containsKey(LISTENERS_KEY)) {
			l.addAll((List<PropertyChangeListener>) named.get(LISTENERS_KEY));
			named.remove(LISTENERS_KEY);
		}
		return l.toArray(new PropertyChangeListener[l.size()]);
	}

	@Override
	public ListenableFuture<Command> background(Object... args)
			throws Exception {
		args(args);
		return environment.executeCommand(this);
	}

	@Override
	public Map<String, Object> getArgs() {
		return args;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getUnnamedArgs() {
		return (List<Object>) args.get(UNNAMED_KEY);
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
		vetoable.fireVetoableChange(INPUT_SOURCE_PROPERTY, null, newValue);
		synchronized (streams) {
			streams.setInputSource(stream);
		}
		log.sourceSet(this, source);
	}

	private InputStream createInputStream(File file)
			throws FileNotFoundException {
		return new FileInputStream(file);
	}

	@Override
	public InputStream getInput() {
		return streams.getInputSource();
	}

	@Override
	public Command input(Object source) throws Exception {
		setInput(source);
		return this;
	}

	@Override
	public void setOutput(Object target) throws Exception {
		setOutput(target, false);
	}

	@Override
	public void setOutput(Object target, boolean append) throws Exception {
		setOutput(STANDRD_OUTPUT_DESCRIPTOR, target, append);
	}

	@Override
	public void setOutput(int descriptor, Object target, boolean append)
			throws Exception {
		log.checkTarget(this, target);
		Object newValue;
		OutputStream stream;
		if (target instanceof OutputStream) {
			newValue = target;
			stream = (OutputStream) target;
		} else if (target instanceof File) {
			newValue = target;
			stream = createOutputStream((File) target, append);
		} else {
			newValue = new File(target.toString());
			stream = createOutputStream((File) newValue, append);
		}
		vetoable.fireVetoableChange(OUTPUT_TARGET_PROPERTY, null, newValue);
		synchronized (streams) {
			streams.setOutputTarget(descriptor, stream);
		}
		log.outputTargetSet(this, target);
	}

	private OutputStream createOutputStream(File file, boolean append)
			throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	@Override
	public OutputStream getOutput() {
		return streams.getOutputTarget();
	}

	@Override
	public Command output(Object target) throws Exception {
		setOutput(target, false);
		return this;
	}

	@Override
	public void setError(Object target) throws Exception {
		setError(target, false);
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
		vetoable.fireVetoableChange(ERROR_TARGET_PROPERTY, null, newValue);
		synchronized (streams) {
			streams.setErrorTarget(stream);
		}
		log.errorTargetSet(this, target);
	}

	@Override
	public OutputStream getError() {
		return streams.getErrorTarget();
	}

	@Override
	public Command error(Object target) throws Exception {
		setError(target);
		return this;
	}

	@Override
	public void setPipeBufferSize(int size) {
		log.checkPipeBuffer(this, size);
		this.pipeBufferSize = size;
		log.pipeBufferSizeSet(this, size);
	}

	@Override
	public int getPipeBufferSize() {
		return pipeBufferSize;
	}

	@Override
	public Command pipe(Command rhs) throws Exception {
		int pipeSize = getPipeBufferSize();
		PipedInputStream sink = new PipedInputStream(pipeSize);
		PipedOutputStream target = new PipedOutputStream(sink);
		setOutput(target);
		rhs.setInput(sink);
		Future<Command> task = environment.executeCommand(this);
		Future<Command> taskRhs = environment.executeCommand(rhs);
		task.get();
		taskRhs.get();
		return rhs;
	}

	/**
	 * @see #pipe(Command)
	 */
	public Command or(final Command rhs) throws Exception {
		return pipe(rhs);
	}

	/**
	 * Operator {@code <<}.
	 * 
	 * @return this {@link Command}.
	 * 
	 * @see #setInput(Object)
	 */
	public Command leftShift(Object rhs) throws Exception {
		setInput(rhs);
		environment.executeCommandAndWait(this);
		return this;
	}

	/**
	 * Operator {@code >>}.
	 * 
	 * @return this {@link Command}.
	 * 
	 * @see #setOutput(Object)
	 */
	public Command rightShift(Object rhs) {
		return this;
	}

	/**
	 * Instantiates the specified type with the default constructor.
	 * 
	 * @param type
	 *            the {@link Class} type.
	 * 
	 * @return the instantiated type.
	 * 
	 * @throws CommandException
	 *             if there were errors instantiate the class type.
	 */
	protected <T> T createType(Class<T> type) throws CommandException {
		try {
			return ConstructorUtils.invokeConstructor(type);
		} catch (NoSuchMethodException e) {
			throw log.noDefaultCtor(this, e, type);
		} catch (IllegalAccessException e) {
			throw log.noDefaultCtor(this, e, type);
		} catch (InvocationTargetException e) {
			throw log.errorInstantiate(this, e, type);
		} catch (InstantiationException e) {
			throw log.errorInstantiate(this, e, type);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getTheName())
				.toString();
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
