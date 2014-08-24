package com.anrisoftware.mongoose.devices.blkidbuildin;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.builder.Builder;

/**
 * Parses the output of the {@code blkid} command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BlkidParser implements Builder<Map<String, String>> {

    private static final String PATTERN_PROPERTY = "blkid_pattern";

    private final String matchPattern;

    private String string;

    private File device;

    private Map<String, String> values;

    @Inject
    BlkidParser(BlkidBuildinProperties p) {
        this.matchPattern = p.get().getProperty(PATTERN_PROPERTY);
    }

    /**
     * Sets the string to parse.
     * 
     * @param string
     *            the {@link String} to parse.
     * 
     * @return this {@link BlkidParser}.
     */
    public BlkidParser withString(String string) {
        this.string = string;
        return this;
    }

    /**
     * Sets the device path.
     * 
     * @param devicePath
     *            the device path.
     * 
     * @return this {@link BlkidParser}.
     */
    public BlkidParser withDevice(File devicePath) {
        this.device = devicePath;
        return this;
    }

    /**
     * Sets values map.
     * 
     * @param values
     *            the {@link Map} that will hold the parsed values.
     * 
     * @return this {@link BlkidParser}.
     */
    public BlkidParser withValues(Map<String, String> values) {
        this.values = values;
        return this;
    }

    /**
     * Parses the values.
     * 
     * @return this {@link Map} with the parsed values.
     */
    @Override
    public Map<String, String> build() {
        Matcher matcher = compile(format(matchPattern, device)).matcher(string);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            values.put(key, value);
        }
        return values;
    }

}
