def printHelp(def parser) {
	echo "Help:"
	echo parser
}

class Parameter {

	@Option(name = "-a", required = true)
	String parameterA

	@Option(name = "-b", required = true)
	int parameterB

	@Option(name = "-c")
	boolean parameterC

	@Argument
	List<String> arguments
}

echo ARGS
parser = parse new Parameter()
if (!parser.isValid) {
	printHelp parser
} else {
	echo parser.parameterA
	echo parser.parameterB
	echo parser.parameterC
	echo parser.arguments
}
