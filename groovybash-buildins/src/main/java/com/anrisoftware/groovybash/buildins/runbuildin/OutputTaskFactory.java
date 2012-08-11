package com.anrisoftware.groovybash.buildins.runbuildin;

import java.io.PrintStream;

interface OutputTaskFactory {

	OutputTask create(Process process, PrintStream outputStream);
}
