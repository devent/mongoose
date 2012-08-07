package com.anrisoftware.groovybash.parameter

echo

echo "Hello World"

echo "Hello World", "Hello again"

var = "Hello Var"
echo var

echo "$var in String"

ret = echo "Something"
echo ret
assert ret == true
