package com.anrisoftware.groovybash.core.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.anrisoftware.groovybash.core.api.ExecutorServiceHandler;

class FixedThreadPoolExecutorServiceHandler implements ExecutorServiceHandler {

	private final ExecutorService service;

	public FixedThreadPoolExecutorServiceHandler(int maxThreads) {
		this.service = Executors.newFixedThreadPool(maxThreads);
	}

	@Override
	public Future<?> submitTask(Runnable task) {
		return service.submit(task);
	}

}
