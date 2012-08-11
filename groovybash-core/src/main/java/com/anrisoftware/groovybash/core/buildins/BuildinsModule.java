/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of groovybash-core.
 * 
 * groovybash-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * groovybash-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * groovybash-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.groovybash.core.buildins;

import com.anrisoftware.groovybash.core.buildins.cdbuildin.CdModule;
import com.anrisoftware.groovybash.core.buildins.echobuildin.EchoModule;
import com.anrisoftware.groovybash.core.buildins.listfilesbuildin.ListFilesModule;
import com.anrisoftware.groovybash.core.buildins.logbuildins.DebugModule;
import com.anrisoftware.groovybash.core.buildins.logbuildins.ErrorModule;
import com.anrisoftware.groovybash.core.buildins.logbuildins.InfoModule;
import com.anrisoftware.groovybash.core.buildins.logbuildins.TraceModule;
import com.anrisoftware.groovybash.core.buildins.logbuildins.WarnModule;
import com.anrisoftware.groovybash.core.buildins.parsebuildin.ParseModule;
import com.anrisoftware.groovybash.core.buildins.pwdbuildin.PwdModule;
import com.anrisoftware.groovybash.core.buildins.returns.ReturnsModule;
import com.anrisoftware.groovybash.core.buildins.runbuildin.RunModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Install the build-in modules. Provides a default of the the standard input
 * and output streams for build-ins commands.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class BuildinsModule extends AbstractModule {

	@Override
	protected void configure() {
		installBuildins();
	}

	private void installBuildins() {
		install(new CdModule());
		install(new EchoModule());
		install(new ListFilesModule());
		install(new DebugModule());
		install(new ErrorModule());
		install(new InfoModule());
		install(new TraceModule());
		install(new WarnModule());
		install(new ParseModule());
		install(new PwdModule());
		install(new ReturnsModule());
		install(new RunModule());
	}

	/**
	 * Provides a default of the the standard input and output streams for
	 * build-ins commands.
	 * 
	 * @return the {@link StandardStreams} that returns the standard input and
	 *         output streams.
	 */
	@Provides
	protected StandardStreams getStandardStreams() {
		return new StandardStreams(System.in, System.out, System.err);
	}
}
