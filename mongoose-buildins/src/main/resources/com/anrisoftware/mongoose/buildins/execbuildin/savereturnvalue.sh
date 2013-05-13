# output, error, status files
errfile=$(mktemp)
outfile=$(mktemp)
statusfile=$(mktemp)

# execute command
%s
status=$(cat $statusfile)

# output status
cat $outfile
cat $errfile 1>&2

# cleanup
rm $errfile
rm $outfile
rm $statusfile

# return exit value of command
exit $status
