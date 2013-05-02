package com.anrisoftware.mongoose.app;

import java.io.File;

import com.anrisoftware.mongoose.parameter.Parameter;

/**
 * Returns the file name of the script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FileName {

	/**
	 * Returns the file name depending if the script file or script resource was
	 * set.
	 * 
	 * @param parameter
	 *            the application {@link Parameter}.
	 * 
	 * @return the {@link String} file name.
	 */
	public String name(Parameter parameter) {
		if (parameter.getScriptFile() != null) {
			return parameter.getScriptFile().getName();
		} else {
			return new File(parameter.getScriptResource().getPath()).getName();
		}
	}

}
