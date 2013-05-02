package com.anrisoftware.mongoose.app;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main entry for application.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Start {

	public static void main(String[] args) throws AppException {
		Injector injector = Guice.createInjector(new ApplicationModule());
		App app = injector.getInstance(App.class);
		app.start(args);
	}
}
