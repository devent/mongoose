package com.anrisoftware.groovybash.core.buildins.runbuildin;

import java.io.PrintStream;

interface OutputTaskFactory {

	OutputTask create(Process process, PrintStream outputStream);
}
