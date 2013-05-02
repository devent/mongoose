package com.anrisoftware.mongoose.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Start {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ApplicationModule());
		App app = injector.getInstance(App.class);
		app.start(args, injector);
	}
}
