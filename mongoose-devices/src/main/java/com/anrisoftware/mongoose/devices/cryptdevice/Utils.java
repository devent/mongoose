package com.anrisoftware.mongoose.devices.cryptdevice;

import java.util.Map;

import com.anrisoftware.propertiesutils.ContextProperties;

/**
 * Utilities.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
final class Utils {

    public static String getVar(Map<String, String> env, ContextProperties p,
            String key) {
        return env.containsKey(key.toUpperCase()) ? env.get(key.toUpperCase())
                : p.getProperty(key);
    }

}
