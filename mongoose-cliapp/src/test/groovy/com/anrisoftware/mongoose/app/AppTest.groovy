package com.anrisoftware.mongoose.app

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see App
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppTest {

	@Test(timeout = 1000l)
	void "scipt file [empty]"() {
		def file = folder.newFile("script.groovy");
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [no args]"() {
		def file = folder.newFile("script.groovy");
		write file, """pwd()"""
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [no args, property]"() {
		def file = folder.newFile("script.groovy");
		write file, '''pwd'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [create]"() {
		def file = folder.newFile("script.groovy");
		write file, '''
def cmd = create name: "pwd" theCommand
cmd()
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [echo]"() {
		def file = folder.newFile("script.groovy");
		write file, """echo \"Hello\""""
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [map args]"() {
		def file = folder.newFile("script.groovy");
		write file, """echo newline: false, \"Hello\""""
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [external]"() {
		def txt = folder.newFile("file.txt");
		write txt, """Hello"""
		def file = folder.newFile("script.groovy");
		write file, """cat \"$txt\""""
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [external, map]"() {
		def txt = folder.newFile("file.txt");
		write txt, """Hello"""
		def file = folder.newFile("script.groovy");
		write file, """cat timeout: 500, \"$txt\""""
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [log context]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
EXECUTION_MODE = ExecutionMode.EXPLICIT
echo debug.theContext
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [println]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
println "Test"
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [background]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
def cmd = create name: "echo" theCommand
def task = cmd.background "Test"
task.get()
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [background, listener]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
def mylistener = { evt -> echo "Done." } as PropertyChangeListener
def cmd = create name: "echo" theCommand
def task = cmd.background listener: mylistener, "Test"
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [background, listeners]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
def mylistenerA = { evt -> echo "Done A." } as PropertyChangeListener
def mylistenerB = { evt -> echo "Done B." } as PropertyChangeListener
def cmd = create name: "echo" theCommand
def task = cmd.background listeners: [mylistenerA, mylistenerB], "Test"
'''
		app.start(["-file", file] as String[])
	}

	@Test(timeout = 1000l)
	void "scipt file [override build-in]"() {
		def file = folder.newFile("Script.groovy");
		write file, '''
def cd() {
  echo "Cd called."
  buildin name: "cd"
}

cd
'''
		app.start(["-file", file] as String[])
	}

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	Injector injector

	App app

	@Before
	void setupApp() {
		injector = Guice.createInjector(new AppModule())
		app = injector.getInstance(App)
	}

	@BeforeClass
	static void setupStringStyle() {
		toStringStyle
	}
}
