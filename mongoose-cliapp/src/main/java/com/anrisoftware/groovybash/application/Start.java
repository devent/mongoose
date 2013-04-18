package com.anrisoftware.groovybash.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Start {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ApplicationModule());
		Application app = injector.getInstance(Application.class);
		app.start(args, injector);
	}
}
