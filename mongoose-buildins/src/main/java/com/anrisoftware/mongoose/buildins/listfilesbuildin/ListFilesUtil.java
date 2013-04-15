package com.anrisoftware.mongoose.buildins.listfilesbuildin;

import static org.apache.commons.io.filefilter.DirectoryFileFilter.DIRECTORY;
import static org.apache.commons.io.filefilter.FalseFileFilter.FALSE;
import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Adapted from {@link FileUtils} and included a depth counter.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ListFilesUtil {

	/**
	 * @see FileUtils#listFiles(File, IOFileFilter, IOFileFilter)
	 * 
	 * @param maxDepth
	 *            the maximum depth of sub-directories.
	 * 
	 * @param includeSubDirectories
	 *            indicates if will include the sub-directories themselves.
	 */
	public static Collection<File> listFiles(File directory,
			IOFileFilter fileFilter, IOFileFilter dirFilter, int maxDepth,
			boolean includeSubDirectories) {
		IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
		IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
		Collection<File> files = new java.util.LinkedList<File>();
		innerListFiles(files, directory,
				FileFilterUtils.or(effFileFilter, effDirFilter), maxDepth, 0,
				includeSubDirectories);
		return files;
	}

	/**
	 * Returns a filter that accepts files in addition to the {@link File}
	 * objects accepted by the given filter.
	 * 
	 * @param fileFilter
	 *            a base {@link IOFileFilter} filter to add to.
	 * 
	 * @return a {@link IOFileFilter} filter that accepts files.
	 */
	private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter fileFilter) {
		return and(fileFilter, notFileFilter(DIRECTORY));
	}

	/**
	 * Returns a filter that accepts directories in addition to the {@link File}
	 * objects accepted by the given filter.
	 * 
	 * @param dirFilter
	 *            a base {@link IOFileFilter} filter to add to.
	 * 
	 * @return a {@link IOFileFilter} filter that accepts directories
	 */
	private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter dirFilter) {
		return dirFilter == null ? FALSE : and(dirFilter, DIRECTORY);
	}

	/**
	 * Finds files within a given directory (and optionally its
	 * sub-directories). All files found are filtered by an {@link IOFileFilter}
	 * .
	 * 
	 * @param files
	 *            the collection of files found.
	 * 
	 * @param directory
	 *            the directory to search in.
	 * 
	 * @param filter
	 *            the filter to apply to files and directories.
	 * 
	 * @param maxDepth
	 *            the maximum depth of sub-directories.
	 * 
	 * @param depth
	 *            the current depth of sub-directories.
	 * 
	 * @param includeSubDirectories
	 *            indicates if will include the sub-directories themselves.
	 */
	private static void innerListFiles(Collection<File> files, File directory,
			IOFileFilter filter, int maxDepth, int depth,
			boolean includeSubDirectories) {
		if (depth > maxDepth) {
			return;
		}
		File[] found = directory.listFiles((FileFilter) filter);
		if (found == null) {
			return;
		}
		for (File file : found) {
			if (file.isDirectory()) {
				if (includeSubDirectories) {
					files.add(file);
				}
				innerListFiles(files, file, filter, maxDepth, depth + 1,
						includeSubDirectories);
			} else {
				files.add(file);
			}
		}
	}
}
