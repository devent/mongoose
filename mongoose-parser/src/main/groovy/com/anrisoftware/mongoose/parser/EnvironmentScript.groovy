package com.anrisoftware.mongoose.parser;

import org.codehaus.groovy.runtime.InvokerHelper

import com.anrisoftware.mongoose.api.environment.Environment

/**
 * Delegates missing methods and missing properties to the environemnt.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class EnvironmentScript extends Script {

	private Environment environment;

	/**
	 * Sets the environment.
	 * 
	 * @param environment
	 * 			  the {@link Environment}.
	 */
	void setEnvironment(Environment environment) {
		this.environment = environment;
		setupDelegate environment
	}

	private void setupDelegate(Environment environment) {
		this.metaClass.methodMissing = { name, args ->
			InvokerHelper.invokeMethod(environment, name, args);
		}
		this.metaClass.propertyMissing = { name ->
			InvokerHelper.getProperty(environment, name);
		}
	}

	def getOut() {
		System.out
	}

	@Override
	Object invokeMethod(String name, Object args) {
		try {
			return super.invokeMethod(name, args);
		} catch (MissingMethodException mme) {
			return InvokerHelper.invokeMethod(environment, name, args);
		}
	}

	@Override
	void setProperty(String property, Object newValue) {
		if (environment.hasVariable(property)) {
			environment.setVariable(property, newValue);
		} else {
			super.setProperty(property, newValue);
		}
	}

	@Override
	Object getProperty(String property) {
		if (environment.hasVariable(property)) {
			return environment.getVariable(property);
		} else {
			return super.getProperty(property);
		}
	}
}
