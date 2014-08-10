package com.anrisoftware.mongoose.api.environment;

import static com.anrisoftware.globalpom.format.enums.EnumFormat.createEnumFormat;

import java.text.Format;

/**
 * Policy how to proceed with commands started in the background.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BackgroundCommandsPolicy {

    /**
     * Wait for all background commands to finish.
     */
    WAIT,

    /**
     * Wait for all background commands to finish with an infinite timeout.
     */
    WAIT_NO_TIMEOUT,

    /**
     * Cancel all background commands.
     */
    CANCEL;

    /**
     * Formatter for {@link BackgroundCommandsPolicy}.
     */
    public static final Format FORMAT = createEnumFormat(BackgroundCommandsPolicy.class);

    /**
     * Formatter for {@link BackgroundCommandsPolicy}.
     */
    public static Format POLICY_FORMAT = FORMAT;
}
