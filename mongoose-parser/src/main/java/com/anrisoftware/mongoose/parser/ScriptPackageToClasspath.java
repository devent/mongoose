package com.anrisoftware.mongoose.parser;

import static org.apache.commons.lang3.StringUtils.replace;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.propertiesutils.ContextProperties;

class ScriptPackageToClasspath {

	private static final String SCRIPT_PACKAGE_TO_CLASSPATH_PROPERTY = "script_package_to_classpath";

	private final boolean scriptPackageToClasspath;

	private final ScriptPackageToClasspathLogger log;

	/**
	 * 
	 * @param p
	 *            the {@link ContextProperties} properties:
	 *            <p>
	 *            <dl>
	 * 
	 *            <dt>
	 *            {@code com.anrisoftware.mongoose.parser.script_package_to_classpath}
	 *            </dt>
	 *            <dd>If the script package should be added to the script class
	 *            path. Default to {@code true}.</dd>
	 * 
	 *            </dl>
	 */
	@Inject
	ScriptPackageToClasspath(ScriptPackageToClasspathLogger logger,
			@Named("parser-properties") ContextProperties p) {
		this.log = logger;
		this.scriptPackageToClasspath = p.getBooleanProperty(
				SCRIPT_PACKAGE_TO_CLASSPATH_PROPERTY, true);
	}

	/**
	 * Adds the script package path to the script class path. Only the script
	 * home path is added if the script itself does not provide any package
	 * information.
	 * 
	 * @param script
	 *            the {@link Script}.
	 * 
	 * @param scriptHome
	 *            the home directory of the script.
	 * 
	 * @see Class#getPackage()
	 */
	public void addPackageNameToClassPath(Script script, String scriptHome) {
		if (scriptPackageToClasspath) {
			addPackageNameToClassPath0(script, scriptHome);
		}
	}

	private void addPackageNameToClassPath0(Script script, String scriptHome) {
		String path;
		GroovyClassLoader loader;
		loader = (GroovyClassLoader) script.getClass().getClassLoader();
		Package scriptPackage = script.getClass().getPackage();
		if (scriptPackage == null) {
			path = scriptHome;
		} else {
			String name = replace(scriptPackage.getName(), ".", "/");
			path = new File(scriptHome, name).getAbsolutePath();
		}
		loader.addClasspath(path);
		log.pathAdded(path);
	}

}
