/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-buildins.
 * 
 * groovybash-buildins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-buildins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-buildins. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.buildins.pwdbuildin

import org.junit.Before
import org.junit.Test

import com.anrisoftware.groovybash.buildins.BuildinTestUtils
import com.anrisoftware.mongoose.buildins.pwdbuildin.PwdBuildin;
import com.anrisoftware.mongoose.buildins.pwdbuildin.PwdModule;

/**
 * Test the build-in command {@code pwd}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ParseTest extends BuildinTestUtils {

	@Before
	void beforeTest() {
		super.beforeTest()
		injector = injector.createChildInjector new PwdModule()
	}

	@Test
	void "pwd buildin with no arguments"() {
		def result = createBuildin(PwdBuildin)()
		assert result.dir == environment.workingDirectory
	}

	@Test
	void "pwd buildin equals file"() {
		def result = createBuildin(PwdBuildin)()
		assert result == environment.workingDirectory
	}
}
