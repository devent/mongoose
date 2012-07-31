package com.anrisoftware.groovybash.core.parser

import com.anrisoftware.groovybash.core.api.Environment

class ParserMetaClass {

	Script setDelegate(Script script, Environment environment) {
		script.metaClass.methodMissing = { name, args ->
			environment.invokeMethod(name, args)
		}
		script.metaClass.propertyMissing = { name ->
			environment.getProperty(name)
		}
		return script
	}
}
