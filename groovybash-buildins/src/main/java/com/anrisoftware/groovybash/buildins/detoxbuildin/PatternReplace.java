package com.anrisoftware.groovybash.buildins.detoxbuildin;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

/**
 * Search for a pattern in the specified string and replace that pattern.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class PatternReplace implements ReplaceString {

	private final Pattern pattern;

	private final String replaceString;

	/**
	 * Sets the pattern and the replace string.
	 * 
	 * @param replace
	 *            the replace string.
	 * 
	 * @param pattern
	 *            the pattern.
	 * 
	 * @param args
	 *            the format arguments for the pattern.
	 * 
	 * @see String#format(String, Object...)
	 */
	public PatternReplace(String replace, String pattern, Object... args) {
		this.pattern = compile(format(pattern, args));
		this.replaceString = replace;
	}

	@Override
	public String replaceString(String string) {
		return pattern.matcher(string).replaceAll(replaceString);
	}
}
