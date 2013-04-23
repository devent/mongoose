package com.anrisoftware.mongoose.resources

import java.beans.PropertyChangeSupport
import java.util.Locale.Category

/**
 * Different hooks to the locale class.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocaleHooks {

	public static final String DISPLAY_LOCALE_PROPERTY = "display_locale"

	public static final String FORMAT_LOCALE_PROPERTY = "format_locale"

	/**
	 * Fire a property change event if the default locale is set.
	 * <p>
	 * Set hooks for {@link Locale#setDefault(Locale.Category, Locale)}
	 * and {@link Locale#setDefault(Locale)} methods.
	 * 
	 * @param change
	 * 			  the {@link PropertyChangeSupport} that contains the 
	 * 			  property change listeners.
	 */
	void hookDefaultLocale(PropertyChangeSupport change) {
		def setDefaultCategoryMethod = Locale.metaClass.getStaticMetaMethod("setDefault", Locale.Category.DISPLAY, Locale.getDefault())
		Locale.metaClass.static.setDefault = { Locale.Category category, Locale newLocale ->
			Locale oldLocale = Locale.getDefault(category)
			setDefaultCategoryMethod.invoke(null, category, newLocale)
			switch (category) {
				case Locale.Category.DISPLAY:
					change.firePropertyChange(DISPLAY_LOCALE_PROPERTY, oldLocale, newLocale)
					break;
				case Locale.Category.FORMAT:
					change.firePropertyChange(FORMAT_LOCALE_PROPERTY, oldLocale, newLocale)
					break;
				default:
					assert false: "Unknown Category";
			}
		}
		def setDefault = Locale.metaClass.getStaticMetaMethod("setDefault", Locale.getDefault())
		Locale.metaClass.static.setDefault = { Locale newLocale ->
			Locale oldLocale = Locale.getDefault()
			Locale.setDefault(Category.DISPLAY, newLocale);
			Locale.setDefault(Category.FORMAT, newLocale);
			setDefault.invoke(null, newLocale)
		}
	}
}
