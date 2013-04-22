package com.anrisoftware.mongoose.resources.threads;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

@SuppressWarnings("serial")
public class ThreadsException extends Exception {

	private Context<ThreadsException> context;

	public ThreadsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThreadsException(String message) {
		super(message);
	}

	public ThreadsException addContext(String name, Object value) {
		return context.addContext(name, value);
	}

	public Map<String, Object> getContext() {
		return context.getContext();
	}

}
