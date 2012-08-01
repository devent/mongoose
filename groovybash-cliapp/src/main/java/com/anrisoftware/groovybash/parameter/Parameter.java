package com.anrisoftware.groovybash.parameter;

import java.io.File;
import java.nio.charset.Charset;

/**
 * The parameter needed to start the Groovy Bash script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Parameter {

	File getScript();

	Charset getCharset();
}
