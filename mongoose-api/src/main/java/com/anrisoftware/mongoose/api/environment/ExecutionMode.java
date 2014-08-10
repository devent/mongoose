package com.anrisoftware.mongoose.api.environment;

import static com.anrisoftware.globalpom.format.enums.EnumFormat.createEnumFormat;

import java.text.Format;

/**
 * The execution mode specifies how commands without arguments are executed.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum ExecutionMode {

    /**
     * Commands without arguments are executed when they are encountered in the
     * script; no parenthesis needed.
     */
    IMPLICIT,

    /**
     * Commands without arguments are executed only if parenthesis are given.
     */
    EXPLICIT;

    /**
     * {@link Format} to parse and format the execution modes.
     */
    public static final Format FORMAT = createEnumFormat(ExecutionMode.class);

    /**
     * {@link Format} to parse and format the execution modes.
     */
    public static final Format EXECUTION_MODE_FORMAT = FORMAT;
}
