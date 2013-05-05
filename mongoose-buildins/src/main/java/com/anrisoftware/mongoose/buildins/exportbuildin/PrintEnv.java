package com.anrisoftware.mongoose.buildins.exportbuildin;

import java.util.Map;

import com.anrisoftware.globalpom.strings.MapToTableString;

class PrintEnv implements ExportWorker {

	@Override
	public void doEnv(Map<String, String> currentEnv, Map<String, String> env,
			Appendable out) {
		MapToTableString.withDefaults(out).append(currentEnv);
	}

}
