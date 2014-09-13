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
package com.anrisoftware.mongoose.api.exceptions;

import java.io.IOException;
import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Thrown if the command encounters an error while executing.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CommandException extends IOException {

    private final Context<CommandException> context;

    /**
     * @see IOException#IOException(String, Throwable)
     */
    public CommandException(Object message, Throwable cause) {
        super(message.toString(), cause);
        this.context = new Context<CommandException>(this);
    }

    /**
     * @see IOException#IOException(String)
     */
    public CommandException(Object message) {
        super(message.toString());
        this.context = new Context<CommandException>(this);
    }

    /**
     * @see Context#addContext(String, Object)
     */
    public CommandException add(Object name, Object value) {
        return context.addContext(name.toString(), value);
    }

    /**
     * @see Context#getContext()
     */
    public Map<String, Object> getContext() {
        return context.getContext();
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
