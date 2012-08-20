#!/usr/bin/env rungroovybash
package com.anrisoftware.groovybash.convertfiles

parameter = parse new Parameter()
parameter.valid { convertFiles() } notValid {
	printHelp()
	return 1
}

class Parameter {

	@Option(name = "-format", aliases = ["-f"], required = true)
	Format format

	@Option(name = "-override", aliases = ["-O"])
	boolean override = false

	@Option(name = "-keep-toxic", aliases = ["-keep", "-k"])
	boolean keepToxic = false

	@Option(name = "-delete", aliases = ["-D"])
	boolean delete = false

	@Option(name = "-preserve-time", aliases = ["-p"])
	boolean preserveTime = false

	@Argument(required = true, metaVar = "FILES")
	List<String> arguments
}

enum Format {
	theora({ inputfile, outputfile ->  "ffmpeg2theora -o ${outputfile} ${inputfile}" }, ".ogv"),

	ffmpegtheora({ inputfile, outputfile ->  "ffmpeg -y -i $inputfile -vcodec libtheora -b 1024k -acodec libvorbis -ab 128k ${outputfile}" }, ".ogv"),

	mp3({ inputfile, outputfile ->  "ffmpeg -y -i $inputfile -ab 128 -ar 44100 ${outputfile}" }, ".mp3");

	def getCommand

	def extention

	Format(def command, def extention) {
		this.getCommand = command
		this.extention = extention
	}
}

def convertFiles() {
	def files = listFiles parameter.arguments
	files.each {
		def lastModifiedTime = it.lastModified()
		def file = detoxFile it, lastModifiedTime
		convertFile file, parameter.format, lastModifiedTime
		deleteFile file
	}
}

File detoxFile(File file, def lastModifiedTime) {
	def name = detox file
	if (name == file || name.file.isFile()) {
		return file
	}
	parameter.keepToxic ?
					copyDetoxFile(file, name.file) :
					moveDetoxFile(file, name.file)
	updateLastModifiedTime name.file, lastModifiedTime
	return name.file
}

def copyDetoxFile(File source, File destination) {
	FileUtils.copyFile source, destination, parameter.preserveTime
	info TEXTS.Logging.copied_file, source, destination
}

def moveDetoxFile(File source, File destination) {
	FileUtils.moveFile source, destination
	destination.setLastModified Calendar.instance.timeInMillis
	info TEXTS.Logging.moved_file, source, destination
}

def updateLastModifiedTime(File file, long time) {
	if (!parameter.preserveTime) {
		return
	}
	def success = file.setLastModified(time)
	if (!success) {
		warn TEXTS.Logging.preserve_time_not_successull, file
	}
}

def convertFile(File file, Format format, def lastModifiedTime) {
	def name = FilenameUtils.removeExtension file.absolutePath
	File outputfile = "$name${format.extention}"as File
	if (!parameter.override && outputfile.isFile()) {
		info TEXTS.Logging.ignore_exists_output_file, outputfile
		return
	}
	def command = format.getCommand file, outputfile
	info TEXTS.Logging.run_command, command
	run "nice $command"
	updateLastModifiedTime outputfile, lastModifiedTime
}

def deleteFile(File file) {
	if (!parameter.delete) {
		return
	}
	file.delete()
	info TEXTS.Logging.delete_original_file, file
}

def printHelp() {
	def programname = 'ConvertFiles.groovy'
	echo TEMPLATES.Documentation.man_page(
					"programname", programname,
					"singleLineUsage", parameter.singleLineUsage,
					"formats", Format.values())
}

