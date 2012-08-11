package com.anrisoftware.groovybash.buildins.runbuildin;

import java.io.PrintStream;

interface ErrorTaskFactory {

	ErrorTask create(Process process, PrintStream errorStream);
}
