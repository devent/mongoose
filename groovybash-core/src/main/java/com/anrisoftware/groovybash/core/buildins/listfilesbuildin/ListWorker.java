package com.anrisoftware.groovybash.core.buildins.listfilesbuildin;

import java.io.File;
import java.util.List;

interface ListWorker {

	List<File> listFiles(File dir);
}
