package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import com.anrisoftware.globalpom.strings.MapToTableString;

/**
 * Print the environment variables.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PrintEnv implements ExportWorker {

	@Override
	public void doEnv(Map<String, String> currentEnv, Map<String, String> env,
			OutputStream out) {
		PrintStream print = new PrintStream(out);
		MapToTableString.withDefaults(print).append(currentEnv);
		print.flush();
	}

}
