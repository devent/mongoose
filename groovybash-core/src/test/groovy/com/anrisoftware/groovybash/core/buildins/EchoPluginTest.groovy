package com.anrisoftware.groovybash.core.buildins

import net.xeoh.plugins.base.options.getplugin.OptionCapabilities

import org.junit.Test

import com.anrisoftware.groovybash.core.CommandTestUtils
import com.anrisoftware.groovybash.core.api.Buildin
import com.anrisoftware.groovybash.core.api.BuildinPlugin

/**
 * Test the build-in command {@link EchoBuildin} loaded as plug-in.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class EchoPluginTest extends CommandTestUtils {

	@Test
	void "echo no arguments"() {
		BuildinPlugin plugin = createPluginManager().getPlugin BuildinPlugin, new OptionCapabilities("buildin:echo")
		Buildin buildin = plugin.getBuildin injector
		assert buildin != null
	}
}

