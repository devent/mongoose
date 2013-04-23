package com.anrisoftware.mongoose.resources

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

import org.junit.Before
import org.junit.Test
import org.perfidix.Benchmark
import org.perfidix.annotation.Bench
import org.perfidix.ouput.TabularSummaryOutput

/**
 * @see LocaleDelegate
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocaleDelegateTest {

	@Test
	void "delegate set default +category"() {
		Locale.setDefault(Locale.Category.DISPLAY, new Locale("xx"))
		assert displayLocaleChanged == true
	}

	@Test
	void "delegate set default"() {
		Locale.setDefault(new Locale("yy"))
		assert displayLocaleChanged == true
	}

	@Test
	void "benchmark delegate set default"() {
		def benchmark = new Benchmark();
		benchmark.add this
		def result = benchmark.run()
		new TabularSummaryOutput().visitBenchmark(result);
	}

	@Bench(runs = 100)
	void "bench delegate set default +catgory"() {
		Locale.setDefault(Locale.Category.DISPLAY, Locale.GERMAN)
	}

	@Bench(runs = 100)
	void "bench delegate set default"() {
		Locale.setDefault(Locale.GERMAN)
	}

	PropertyChangeSupport support

	PropertyChangeListener listener

	boolean displayLocaleChanged

	@Before
	void createSupport() {
		support = new PropertyChangeSupport(this)
		displayLocaleChanged = false
		listener = { evt -> displayLocaleChanged = true } as PropertyChangeListener
		support.addPropertyChangeListener(LocaleHooks.DISPLAY_LOCALE_PROPERTY, listener)
		new LocaleHooks().hookDefaultLocale(support)
	}
}
