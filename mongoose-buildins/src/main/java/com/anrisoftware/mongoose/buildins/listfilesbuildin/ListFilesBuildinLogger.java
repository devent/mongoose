package com.anrisoftware.mongoose.buildins.listfilesbuildin;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.FileFilter;
import java.lang.reflect.Constructor;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ListFilesBuildin}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ListFilesBuildinLogger extends AbstractLogger {

	private static final String NO_CONSTRUCTOR = "No default constructor or constructor with string argument found for type %s.";
	private static final String SET_INCLUDE_SUB_DIRECTORIES = "Set include sub directories {} for {}.";
	private static final String SET_RECURSIVE = "Set recursive {} for {}.";
	private static final String SET_DEPTH = "Set depth {} for {}.";
	private static final String SET_FILTER = "Set filter {} for {}.";
	private static final String FILTER_NULL = "The filter cannot be null.";
	private static final String DEPTH_NUMBER = "The depth can be -1 or a (+)number.";

	/**
	 * Create logger for {@link ListFilesBuildin}.
	 */
	public ListFilesBuildinLogger() {
		super(ListFilesBuildin.class);
	}

	void checkDepth(ListFilesBuildin buildin, int depth) {
		inclusiveBetween(-1, Integer.MAX_VALUE, depth, DEPTH_NUMBER);
	}

	void checkFilter(ListFilesBuildin buildin, Object filter) {
		notNull(filter, FILTER_NULL);
	}

	void filterSet(ListFilesBuildin buildin, FileFilter filter) {
		log.debug(SET_FILTER, filter, buildin);
	}

	void depthSet(ListFilesBuildin buildin, int depth) {
		log.debug(SET_DEPTH, depth, buildin);
	}

	void recursiveSet(ListFilesBuildin buildin, boolean recursive) {
		log.debug(SET_RECURSIVE, recursive, buildin);
	}

	void includeSubDirectoriesSet(ListFilesBuildin buildin, boolean include) {
		log.debug(SET_INCLUDE_SUB_DIRECTORIES, include, buildin);
	}

	void checkCtor(ListFilesBuildin buildin, Constructor<?> ctor, Class<?> type) {
		notNull(ctor, NO_CONSTRUCTOR, type);
	}
}
