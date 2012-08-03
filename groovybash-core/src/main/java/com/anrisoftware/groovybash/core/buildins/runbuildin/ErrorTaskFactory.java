package com.anrisoftware.groovybash.core.buildins.runbuildin;

import java.io.PrintStream;

interface ErrorTaskFactory {

	ErrorTask create(Process process, PrintStream errorStream);
}
