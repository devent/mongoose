package com.anrisoftware.mongoose.resources

import org.junit.Test
import org.perfidix.Benchmark
import org.perfidix.annotation.Bench
import org.perfidix.ouput.TabularSummaryOutput

/**
 * @see Locale
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LocaleBenchTest {

	@Test
	void "benchmark set default"() {
		def benchmark = new Benchmark();
		benchmark.add this
		def result = benchmark.run()
		new TabularSummaryOutput().visitBenchmark(result);
	}

	@Bench(runs = 100)
	void "bench set default +category"() {
		Locale.setDefault(Locale.Category.DISPLAY, Locale.GERMAN)
	}

	@Bench(runs = 100)
	void "bench set default"() {
		Locale.setDefault(Locale.GERMAN)
	}
}
