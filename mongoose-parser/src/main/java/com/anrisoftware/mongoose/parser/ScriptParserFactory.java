/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.mongoose.parser;

import java.io.Reader;

/**
 * Factory to create a new parser of Groovy scripts.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ScriptParserFactory {

	/**
	 * Creates a new parser for the specified script.
	 * 
	 * @param source
	 *            the {@link Reader} source of the script.
	 * 
	 * @param fileName
	 *            the file name of the script, which is used to create the class
	 *            name of the script.
	 * 
	 * @return the {@link ScriptParser}.
	 */
	ScriptParser create(Reader source, String fileName);
}
