package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.Utils.getVar;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.anrisoftware.mongoose.api.commans.Command;
import com.anrisoftware.mongoose.api.environment.Environment;
import com.anrisoftware.mongoose.command.CommandLoader;
import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Queries the crypt container for its properties.
 * 
 * <ul>
 * <li>type</li>
 * <li>cipher</li>
 * <li>keysize</li>
 * <li>device</li>
 * <li>loop</li>
 * <li>offset</li>
 * <li>size</li>
 * <li>mode</li>
 * </ul>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatusTask {

    private static final String MATCH_TYPE_PROPERTY = "match_type_template";

    private static final String MATCH_STATUS_PROPERTY = "match_status_template";

    private static final String SUDO_COMMAND = "sudo";

    private static final String CRYPTSETUP_PROPERTY = "cryptsetup_command";

    private static final String MATCH_CIPHER_PROPERTY = "match_cipher_template";

    private static final String MATCH_KEYSIZE_PROPERTY = "match_keysize_template";

    private static final String MATCH_DEVICE_PROPERTY = "match_device_template";

    private static final String MATCH_LOOP_PROPERTY = "match_loop_template";

    private static final String MATCH_OFFSET_PROPERTY = "match_offset_template";

    private static final String MATCH_SIZE_PROPERTY = "match_size_template";

    private static final String MATCH_MODE_PROPERTY = "match_mode_template";

    private static final String MATCH_INACTIVE_PROPERTY = "match_inactive_template";

    private final CryptDevice device;

    private final Environment environment;

    private final String newLine;

    @Inject
    private StatusTaskLogger log;

    @Inject
    private CommandLoader loader;

    private String cryptsetupCommand;

    private String matchStatusTemplate;

    private String matchCipherTemplate;

    private String matchKeysizeTemplate;

    private String matchDeviceTemplate;

    private String matchLoopTemplate;

    private String matchOffsetTemplate;

    private String matchSizeTemplate;

    private String matchModeTemplate;

    private String matchTypeTemplate;

    private String matchInactiveTemplate;

    private boolean status;

    private String type;

    private String cipher;

    private String keysize;

    private String devicePath;

    private String loop;

    private String offset;

    private String size;

    private String mode;

    /**
     * @see StatusTaskFactory#create(CryptDevice)
     */
    @Inject
    StatusTask(@Assisted CryptDevice device) {
        this.device = device;
        this.environment = device.getTheEnvironment();
        this.newLine = System.getProperty("line.separator");
    }

    @Inject
    public void setProperties(CryptDevicePropertiesProvider properties) {
        setupProperties(environment.getEnv(), properties.get());
    }

    public void checkDevice(String name) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Map<String, Object> args = device.getArgs();
        args.put("successExitValues", null);
        Command cmd = loader.createCommand(SUDO_COMMAND,
                device.getTheEnvironment(), args, stream, device.getError(),
                device.getInput(),
                format("%s status %s", cryptsetupCommand, name));
        environment.executeCommandAndWait(cmd);
        String outputstr = stream.toString();
        String[] outputlines = split(outputstr, newLine);
        for (String line : outputlines) {
            matchStatus(name, line);
            matchInactive(name, line);
            matchType(line);
            matchCipher(line);
            matchKeysize(line);
            matchDevice(line);
            matchLoop(line);
            matchOffset(line);
            matchSize(line);
            matchMode(line);
        }
        log.cryptsetupOutput(device, outputstr);
    }

    public boolean isStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getCipher() {
        return cipher;
    }

    public String getKeysize() {
        return keysize;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public String getLoop() {
        return loop;
    }

    public String getOffset() {
        return offset;
    }

    public long getSize() {
        String[] split = split(size, " ");
        return Long.valueOf(split[0]);
    }

    public String getMode() {
        return mode;
    }

    private void matchStatus(String name, String line) {
        Matcher matcher = compile(
                formatTemplate(matchStatusTemplate, "name", name))
                .matcher(line);
        if (matcher.matches()) {
            this.status = true;
        }
    }

    private void matchInactive(String name, String line) {
        Matcher matcher = compile(
                formatTemplate(matchInactiveTemplate, "name", name)).matcher(
                line);
        if (matcher.matches()) {
            this.status = false;
        }
    }

    private void matchType(String line) {
        Matcher matcher = compile(matchTypeTemplate).matcher(line);
        if (matcher.matches()) {
            this.type = matcher.group(1);
        }
    }

    private void matchCipher(String line) {
        Matcher matcher = compile(matchCipherTemplate).matcher(line);
        if (matcher.matches()) {
            this.cipher = matcher.group(1);
        }
    }

    private void matchKeysize(String line) {
        Matcher matcher = compile(matchKeysizeTemplate).matcher(line);
        if (matcher.matches()) {
            this.keysize = matcher.group(1);
        }
    }

    private void matchDevice(String line) {
        Matcher matcher = compile(matchDeviceTemplate).matcher(line);
        if (matcher.matches()) {
            this.devicePath = matcher.group(1);
        }
    }

    private void matchLoop(String line) {
        Matcher matcher = compile(matchLoopTemplate).matcher(line);
        if (matcher.matches()) {
            this.loop = matcher.group(1);
        }
    }

    private void matchOffset(String line) {
        Matcher matcher = compile(matchOffsetTemplate).matcher(line);
        if (matcher.matches()) {
            this.offset = matcher.group(1);
        }
    }

    private void matchSize(String line) {
        Matcher matcher = compile(matchSizeTemplate).matcher(line);
        if (matcher.matches()) {
            this.size = matcher.group(1);
        }
    }

    private void matchMode(String line) {
        Matcher matcher = compile(matchModeTemplate).matcher(line);
        if (matcher.matches()) {
            this.mode = matcher.group(1);
        }
    }

    private String formatTemplate(String template, Object... args) {
        ST st = new ST(template);
        for (int i = 0; i < args.length / 2; i++) {
            st.add(args[i].toString(), args[i + 1]);
        }
        return st.render();
    }

    private void setupProperties(Map<String, String> env, ContextProperties p) {
        this.cryptsetupCommand = getVar(env, p, CRYPTSETUP_PROPERTY);
        this.matchStatusTemplate = p.getProperty(MATCH_STATUS_PROPERTY);
        this.matchInactiveTemplate = p.getProperty(MATCH_INACTIVE_PROPERTY);
        this.matchTypeTemplate = p.getProperty(MATCH_TYPE_PROPERTY);
        this.matchCipherTemplate = p.getProperty(MATCH_CIPHER_PROPERTY);
        this.matchKeysizeTemplate = p.getProperty(MATCH_KEYSIZE_PROPERTY);
        this.matchDeviceTemplate = p.getProperty(MATCH_DEVICE_PROPERTY);
        this.matchLoopTemplate = p.getProperty(MATCH_LOOP_PROPERTY);
        this.matchOffsetTemplate = p.getProperty(MATCH_OFFSET_PROPERTY);
        this.matchSizeTemplate = p.getProperty(MATCH_SIZE_PROPERTY);
        this.matchModeTemplate = p.getProperty(MATCH_MODE_PROPERTY);
    }
}
