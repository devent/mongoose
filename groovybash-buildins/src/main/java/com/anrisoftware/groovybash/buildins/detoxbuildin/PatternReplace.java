/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
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
