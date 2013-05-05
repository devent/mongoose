package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.io.OutputStream;
import java.util.Map;

interface ExportWorker {

	void doEnv(Map<String, String> currentEnv, Map<String, String> env,
			OutputStream out);
}
