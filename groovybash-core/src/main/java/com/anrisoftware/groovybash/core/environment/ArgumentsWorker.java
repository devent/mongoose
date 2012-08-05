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
package com.anrisoftware.groovybash.core.environment;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Creates the flags and arguments for the run build-in command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ArgumentsWorker {

	private ArrayList<Object> args;

	private Map<String, String> flags;

	/**
	 * Creates the flags and arguments for the run build-in command.
	 * 
	 * @param name
	 *            the name of the command to run.
	 * 
	 * @param uargs
	 *            the arguments like the run build-in command is expecting
	 *            arguments: {@code [flags,] [, environment] [, dir]}.
	 *            <p>
	 *            The {@code flags} is an optional {@link Map} with the flag
	 *            {@code key=value} pairs, the {@code environment} is the
	 *            system-dependent mapping from variables to values and
	 *            {@code dir} is the optional working directory.
	 * 
	 * @return this {@link ArgumentsWorker} with the arguments and flags.
	 */
	public ArgumentsWorker createCommandArgs(String name, Object[] uargs) {
		args = newArrayList();
		flags = newHashMap();
		List<Object> command = newArrayList();
		command.add(name);
		int index = 0;
		if (uargs[index] instanceof Map) {
			flags.putAll(asMap(uargs[index]));
			command.add(uargs[++index].toString());
		} else {
			command.add(uargs[index].toString());
		}
		for (int i = index + 1; i < uargs.length; i++) {
			args.add(uargs[i]);
		}
		args.add(0, StringUtils.join(command, " "));
		return this;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> asMap(Object obj) {
		return (Map<String, String>) obj;
	}

	/**
	 * Returns the flags for the command.
	 * 
	 * @return a {@link Map} of the flags.
	 */
	public Map<?, ?> getFlags() {
		return flags;
	}

	/**
	 * Returns the arguments for the command.
	 * 
	 * @return the array of arguments.
	 */
	public Object[] getArgs() {
		return args.toArray();
	}

}
