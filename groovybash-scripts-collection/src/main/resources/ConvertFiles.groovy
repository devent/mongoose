#!/usr/bin/env rungroovybash
package com.anrisoftware.groovybash.convertfiles

parameter = parse new Parameter()
parameter.valid {
    convertFiles()
} notValid { 
    printHelp()
    return 1
}

class Parameter {
    
    @Option(name = "-format", aliases = ["-f"], required = true)
    Format format

    @Option(name = "-override", aliases = ["-O"])
    boolean override = false
    
    @Option(name = "-preserve", aliases = ["-P"])
    boolean preserve = false
    
    @Option(name = "-delete", aliases = ["-D"])
    boolean delete = false
    
    @Argument(required = true, metaVar = "FILES")
    List<String> arguments
}

enum Format {
    theora({ inputfile, outputfile -> 
        "ffmpeg2theora -o ${outputfile} ${inputfile}"
    }, ".ogv"), 
    
    ffmpegtheora({ inputfile, outputfile -> 
        "ffmpeg -y -i $inputfile -vcodec libtheora -b 1024k -acodec libvorbis -ab 128k ${outputfile}"
    }, ".ogv"),
    
    mp3({ inputfile, outputfile -> 
        "ffmpeg -y -i $inputfile -ab 128 -ar 44100 ${outputfile}"
    }, ".mp3");
    
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
        def file = detoxFile it
        convertFile file, parameter.format
        deleteFile file
    }
}

File detoxFile(File file) {
    def name = detox file
    if (name == file) {
        return file
    }
    if (parameter.preserve) {
        FileUtils.copyFile file, name.file
        info TEXTS.Logging.copied_file, file, name
    } else {
        FileUtils.moveFile file, name.file
        info TEXTS.Logging.moved_file, file, name
    }
    return name.file
}

def convertFile(File file, Format format) {
    def name = FilenameUtils.removeExtension file.absolutePath
    File outputfile = "$name${format.extention}"as File
    if (!parameter.override && outputfile.isFile()) {
        info TEXTS.Logging.ignore_exists_output_file, outputfile
        return
    }
    def command = format.getCommand file, outputfile
    info TEXTS.Logging.run_command, command
    run "nice $command"
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

