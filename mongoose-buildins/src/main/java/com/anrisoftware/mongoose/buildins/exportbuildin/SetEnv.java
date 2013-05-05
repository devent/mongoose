package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.io.OutputStream;
import java.util.Map;

/**
 * Sets the specified environment variables.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SetEnv implements ExportWorker {

	@Override
	public void doEnv(Map<String, String> currentEnv, Map<String, String> env,
			OutputStream out) {
		currentEnv.putAll(env);
	}

}
