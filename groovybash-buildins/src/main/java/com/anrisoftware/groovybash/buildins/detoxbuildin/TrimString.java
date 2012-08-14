package com.anrisoftware.groovybash.buildins.detoxbuildin;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Trims the string on both ends.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class TrimString implements ReplaceString {

	@Override
	public String replaceString(String string) {
		return trim(string);
	}

}
