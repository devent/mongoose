package com.anrisoftware.groovybash.buildins.runbuildin

import groovy.util.logging.Slf4j

import org.junit.Test
import org.perfidix.Benchmark
import org.perfidix.annotation.Bench
import org.perfidix.ouput.TabularSummaryOutput

/**
 * Benchmark the output of a process to the standard output stream.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@Slf4j
class OutputTaskTest {

	/**
	 * Outputs a given string in the standard output of the process.
	 * 
	 * @author Erwin Mueller, erwin.mueller@deventm.org
	 * @since 0.1
	 */
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

	def output

	def outputStream

	def bigtext = new BigText().run()

	def smalltext = "small text"

	Process process

	@Test
	void "run benchmarks"() {
		def output = new ByteArrayOutputStream()
		def benchmark = new Benchmark()
		benchmark.add(this)
		def result = benchmark.run()
		new TabularSummaryOutput(new PrintStream(output)).visitBenchmark(result)
		log.info "\n{}", output.toString()
	}

	@Test
	void "read from input and output"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(smalltext)
		def task = new OutputTask(process.inputStream, outputStream)
		task.run()
		assert output.toString() == smalltext
	}

	@Bench(runs = 100, beforeFirstRun = "setup big text")
	void "read from input and output with big text"() {
		def task = new OutputTask(process.inputStream, outputStream)
		task.run()
	}

	void "setup big text"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(bigtext)
	}

	@Bench(runs = 100, beforeFirstRun = "setup small text")
	void "read from input and output with small text"() {
		def task = new OutputTask(process.inputStream, outputStream)
		task.run()
	}

	void "setup small text"() {
		output = new ByteArrayOutputStream()
		outputStream = new PrintStream(output)
		process = new DummyProcess(smalltext)
	}
}
