#!/usr/bin/env rungroovybash
package com.anrisoftware.groovybash.convertfiles

import static org.apache.commons.io.FileUtils.*
import static org.apache.commons.io.FilenameUtils.*

parse valid: { it.format.convertFiles(it) },
notValid: { printHelp() },
new Parameter()
return 0

/**
 * Prints the manual page.
 */
void printHelp() {
	def programname = 'ConvertFiles.groovy'
	echo TEMPLATES.Documentation.man_page(
			"programname", programname,
			"singleLineUsage", parameter.singleLineUsage,
			"formats", Format.values())
}

class Parameter {

	@Option(name = "-format", aliases = ["-f"], required = true)
	Format format

	@Option(name = "-override", aliases = ["-O"])
	boolean override = false

	@Option(name = "-keep-original", aliases = ["-keep", "-k"])
	boolean keepOriginal = false

	@Option(name = "-preserve-time", aliases = ["-p"])
	boolean preserveTime = false

	long maxTmpfileSize = 10**30/2

	@Argument(required = true, metaVar = "FILES")
	List<String> args
}

enum Format {

	/**
	 * Converts to the Theora video format using ffmpeg2theora.
	 */
	theora({ inputfile, outputfile ->  "ffmpeg2theora -o ${outputfile} ${inputfile}" }, ".ogv"),

	/**
	 * Converts to the Theora video format.
	 */
	ffmpegtheora({ inputfile, outputfile ->  "ffmpeg -y -i $inputfile -vcodec libtheora -b:v 1024k -acodec libvorbis -b:a 128k ${outputfile}" }, ".ogv"),

	/**
	 * Converts to the Mp3 audio format.
	 */
	mp3({ inputfile, outputfile ->  "ffmpeg -y -i $inputfile -ab 128 -ar 44100 ${outputfile}" }, ".mp3");

	def command

	def extention

	Format(def command, def extention) {
		this.command = command
		this.extention = extention
	}

	void convertFiles(Parameter parameter) {
		listFiles parameter.args each {
			new ConvertFile(source: it, parameter: parameter)()
		}
	}
}

class ConvertFile {

	File source

	Parameter parameter

	long time

	File tmpfile

	File destination

	void call() {
		time = source.lastModified()
		tmpfile = createTempFile source
		destination = createDestination source
		if (!checkDestination(destination)) {
			return
		}
		try {
			copyFile source, tmpfile
			convertFile tmpfile
			copyFile tmpfile, destination
			deleteOriginal source
			preserveTime destination
		}
		finally {
			deleteFile tmpfile
		}
	}

	/**
	 * Create a temporary file for converting. If the file is too big then
	 * the temporary file is created in the same directory as the source file.
	 */
	File createTempFile(File source) {
		def parent = source.parentFile
		def size = source.length()
		def file = File.createTempFile source.name, null
		if (size > parameter.maxTmpfileSize) {
			file.delete()
			return new File(parent, file.name)
		} else {
			return file
		}
	}

	/**
	 * Create the destination file. The destination file have the same
	 * file name as the source, but with changed file ending.
	 */
	File createDestination(File source) {
		def name = removeExtension source.absolutePath
		return "$name${parameter.format.extention}"as File
	}

	/**
	 * Checks if the destination file can be overriden.
	 */
	boolean checkDestination(File file) {
		if (!parameter.override) {
			if (file.isFile()) {
				info TEXTS.Logging.ignore_exists_output_file, file
				return true
			} else {
				return false
			}
		} else {
			return true
		}
	}

	/**
	 * Sets the last modified time of the destination to the time of the source.
	 */
	def preserveTime(File file) {
		if (parameter.preserveTime) {
			def success = file.setLastModified(time)
			if (!success) {
				warn TEXTS.Logging.preserve_time_not_successull, file
			}
		}
	}

	/**
	 * Converts the input file.
	 */
	def convertFile(File inputFile) {
		def command = parameter.format.command inputFile, destination
		debug TEXTS.Logging.run_command, command
		run "nice $command"
	}

	/**
	 * Deletes the original file.
	 */
	def deleteOriginal(File file) {
		if (!parameter.keepOriginal) {
			file.delete()
			info TEXTS.Logging.delete_original_file, file
		}
	}
}
