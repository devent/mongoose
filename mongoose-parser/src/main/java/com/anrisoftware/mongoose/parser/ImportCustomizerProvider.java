package com.anrisoftware.mongoose.parser;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Provider;

/**
 * Provides the imports customizer for the Groovy script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ImportCustomizerProvider implements Provider<ImportCustomizer> {

	private static final String IMPORTS_PROPERTY = "imports";

	private static final String STAR_IMPORTS_PROPERTY = "star_imports";

	private final List<String> starImports;

	private final List<String> imports;

	/**
	 * @param p
	 *            the {@link ContextProperties} properties:
	 *            <p>
	 *            <dl>
	 *            <dt>{@code com.anrisoftware.mongoose.parser.star_imports}</dt>
	 *            <dd>A list of packages that should be imported in the script.</dd>
	 * 
	 *            <dt>{@code com.anrisoftware.mongoose.parser.imports}</dt>
	 *            <dd>A list of classes that should be imported in the script.</dd>
	 * 
	 *            </dl>
	 */
	@Inject
	ImportCustomizerProvider(@Named("parser-properties") ContextProperties p) {
		this.starImports = p.getListProperty(STAR_IMPORTS_PROPERTY);
		this.imports = p.getListProperty(IMPORTS_PROPERTY);
	}

	@Override
	public ImportCustomizer get() {
		ImportCustomizer customizer = new ImportCustomizer();
		customizer.addStarImports(toArray(starImports));
		customizer.addImports(toArray(imports));
		return customizer;
	}

	private String[] toArray(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

}
