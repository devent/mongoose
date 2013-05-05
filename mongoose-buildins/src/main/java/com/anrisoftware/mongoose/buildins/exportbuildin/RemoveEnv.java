package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.io.OutputStream;
import java.util.Map;

/**
 * Remove the variables names from the environment.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RemoveEnv implements ExportWorker {

	@Override
	public void doEnv(Map<String, String> currentEnv, Map<String, String> env,
			OutputStream out) {
		for (String name : env.keySet()) {
			currentEnv.remove(name);
		}
	}

}
