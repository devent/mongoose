package com.anrisoftware.linuxdevices.command.factories;

import com.anrisoftware.linuxdevices.command.api.ReadStandardStreams;


/**
 * Factory to create standard streams that redirects the output and error stream
 * to memory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface ReadStandardStreamsFactory {

	/**
	 * Creates standard streams that redirect the output and error streams to
	 * memory.
	 * 
	 * @return the {@link ReadStandardStreams}.
	 */
	ReadStandardStreams create();
}
