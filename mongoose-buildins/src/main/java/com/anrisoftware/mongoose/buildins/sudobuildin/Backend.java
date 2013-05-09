package com.anrisoftware.mongoose.buildins.sudobuildin;

import java.util.List;
import java.util.Map;

import com.anrisoftware.mongoose.api.commans.Command;

/**
 * Makes available a method to execute commands with root privileges.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Backend {

	/**
	 * Returns the command to execute commands with root privileges.
	 * 
	 * @param args
	 * 
	 * @param unnamed
	 * 
	 * @return the {@link Command}.
	 * 
	 * @throws Exception
	 */
	Command getBackendCommand(Map<String, Object> args, List<Object> unnamed)
			throws Exception;

}
