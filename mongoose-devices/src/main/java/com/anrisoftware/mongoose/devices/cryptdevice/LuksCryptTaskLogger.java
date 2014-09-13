package com.anrisoftware.mongoose.devices.cryptdevice;

import static com.anrisoftware.mongoose.devices.cryptdevice.LuksCryptTaskLogger._.cryptsetup_output;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link LuksCryptTask}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LuksCryptTaskLogger extends AbstractLogger {

    enum _ {

        cryptsetup_output("Cryptsetup returns '{}' for {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link LuksCryptTask}.
     */
    public LuksCryptTaskLogger() {
        super(LuksCryptTask.class);
    }

    void cryptsetupOutput(CryptDevice device, String outputstr) {
        debug(cryptsetup_output, outputstr, device);
    }
}
