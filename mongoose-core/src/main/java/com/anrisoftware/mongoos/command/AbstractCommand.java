package com.anrisoftware.mongoos.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

	private AbstractCommandLogger log;

	private StandardStreams streams;

	private Map<Object, Object> args;

	private Environment environment;

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
	public Command call(Object args) throws Exception {
		setArgs(args);
		return call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setArgs(Object args) throws Exception {
		log.checkArgs(this, args);
		if (args instanceof Map) {
			this.args = asMap((Map<Object, Object>) args);
		} else if (args instanceof List) {
			this.args = asList((List<Object>) args);
		} else {
			this.args = asObj(args);
		}
		log.argumentsSet(this, args);
	}

	private Map<Object, Object> asMap(Map<Object, Object> args) {
		Map<Object, Object> map = createMap(args.size());
		map.putAll(map);
		return map;
	}

	private Map<Object, Object> asObj(Object obj) {
		Map<Object, Object> map = createMap(1);
		map.put(0, obj);
		return map;
	}

	private Map<Object, Object> asList(List<Object> list) {
		Map<Object, Object> map = createMap(list.size());
		for (int i = 0; i < list.size(); i++) {
			map.put(i, list.get(i));
		}
		return map;
	}

	private Map<Object, Object> createMap(int size) {
		return new LinkedHashMap<Object, Object>(size);
	}

	@Override
	public Command args(Object args) throws Exception {
		setArgs(args);
		return this;
	}

	@Override
	public Map<Object, Object> getArgs() {
		return args;
	}

	@Override
	public void setInputStream(OutputStream stream) {
		streams.setInputStream(stream);
	}

	@Override
	public void setInput(Object source) throws Exception {
		log.checkSource(this, source);
		if (source instanceof InputStream) {
			streams.setInputSource((InputStream) source);
		} else if (source instanceof File) {
			streams.setInputSource(createInputStream((File) source));
		} else {
			streams.setInputSource(createInputStream(new File(source.toString())));
		}
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
		if (target instanceof InputStream) {
			streams.setOutputTarget(descriptor, (OutputStream) target);
		} else if (target instanceof File) {
			streams.setOutputTarget(descriptor,
					createOutputStream((File) target, append));
		} else {
			streams.setOutputTarget(descriptor,
					createOutputStream(new File(target.toString()), append));
		}
		log.outputTargetSet(this, target);
	}

	private OutputStream createOutputStream(File file, boolean append)
			throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	@Override
	public void setErrorStream(InputStream stream) {
		streams.setErrorStream(stream);
	}

	@Override
	public void setError(Object target) throws Exception {
		setError(target);
	}

	@Override
	public void setError(Object target, boolean append) throws Exception {
		log.checkTarget(this, target);
		if (target instanceof OutputStream) {
			streams.setErrorTarget((OutputStream) target);
		} else if (target instanceof File) {
			streams.setErrorTarget(createOutputStream((File) target, append));
		} else {
			streams.setErrorTarget(createOutputStream(
					new File(target.toString()), append));
		}
		log.errorTargetSet(this, target);
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

}
