package com.anrisoftware.groovybash.core.buildins.runbuildin

import groovy.util.logging.Slf4j

import org.junit.Test
import org.perfidix.Benchmark
import org.perfidix.annotation.Bench
import org.perfidix.ouput.TabularSummaryOutput

@Slf4j
class OutputTaskTest {

	static class DummyProcess extends Process {

		String outputString

		DummyProcess(String string) {
			outputString = string
		}

		@Override
		OutputStream getOutputStream() {
		}

		@Override
		InputStream getInputStream() {
			new ByteArrayInputStream(outputString.getBytes("UTF-8"))
		}

		@Override
		InputStream getErrorStream() {
		}

		@Override
		int waitFor() throws InterruptedException {
		}

		@Override
		int exitValue() {
		}

		@Override
		void destroy() {
		}
	}

	@Test
	void "run benchmarks"() {
		def output = new ByteArrayOutputStream()
		def benchmark = new Benchmark()
		benchmark.add(this)
		def result = benchmark.run()
		new TabularSummaryOutput(new PrintStream(output)).visitBenchmark(result)
		log.info "{}\n", output.toString()
	}

	@Test
	void "read from input and output"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(smalltext)
		def task = new OutputTask(process, outputStream)
		task.run()
		assert output.toString() == smalltext
	}

	def output

	def outputStream

	def bigtext = new BigText().run()

	def smalltext = "small text"

	def process

	@Bench(runs = 1000, beforeFirstRun = "setup before read from input and output with big text")
	@Test
	void "read from input and output with big text"() {
		def task = new OutputTask(process, outputStream)
		task.run()
	}

	void "setup before read from input and output with big text"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(bigtext)
	}

	@Bench(runs = 1000, beforeFirstRun = "setup before read from input and output with small text")
	@Test
	void "read from input and output with small text"() {
		def task = new OutputTask(process, outputStream)
		task.run()
	}

	void "setup before read from input and output with small text"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(smalltext)
	}
}
