package com.anrisoftware.mongoose.api.environment;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

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
	@SuppressWarnings("serial")
	public static Format POLICY_FORMAT = new Format() {

		@Override
		public Object parseObject(String source, ParsePosition pos) {
			return parse(source, pos);
		}

		/**
		 * @see #parseObject(String, ParsePosition)
		 */
		public BackgroundCommandsPolicy parse(String source, ParsePosition pos) {
			try {
				source = source.substring(pos.getIndex());
				BackgroundCommandsPolicy item = valueOf(source.toUpperCase());
				pos.setErrorIndex(-1);
				pos.setIndex(source.length());
				return item;
			} catch (IllegalArgumentException e) {
				pos.setIndex(0);
				pos.setErrorIndex(0);
				return null;
			}
		}

		@Override
		public StringBuffer format(Object obj, StringBuffer buff,
				FieldPosition pos) {
			if (obj instanceof BackgroundCommandsPolicy) {
				format(buff, (BackgroundCommandsPolicy) obj);
			}
			return buff;
		}

		private void format(StringBuffer buff, BackgroundCommandsPolicy obj) {
			buff.append(obj.toString());
		}
	};
}
