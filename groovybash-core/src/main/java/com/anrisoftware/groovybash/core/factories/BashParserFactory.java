package com.anrisoftware.groovybash.core.factories;

import com.anrisoftware.groovybash.core.parser.BashParser;

public interface BashParserFactory {

	BashParser create(String script);
}
