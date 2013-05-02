package com.anrisoftware.mongoose.parameter;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * The parameter needed to start the Groovy Bash script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Parameter {

	/**
	 * Returns the script file.
	 * 
	 * @return the {@link File}.
	 */
	File getScriptFile();

	/**
	 * Returns the script resource.
	 * 
	 * @return the {@link URL} of the resource.
	 */
	URL getScriptResource();

	/**
	 * Returns the script character set.
	 * 
	 * @return the {@link Charset}.
	 */
	Charset getCharset();

	/**
	 * Returns the command line arguments of the script.
	 * 
	 * @return the command line arguments {@link String} array.
	 */
	String[] getArgs();

}
