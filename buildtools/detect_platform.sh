#!/bin/bash

if [[ "$OSTYPE" == "linux-gnu" ]]; then
    SO_SUFFIX=".so"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    SO_SUFFIX=".dylib"
elif [[ "$OSTYPE" == "cygwin" ]]; then
    SO_SUFFIX=".dll"
elif [[ "$OSTYPE" == "msys" ]]; then
    SO_SUFFIX=".dll"
elif [[ "$OSTYPE" == "win32" ]]; then
    SO_SUFFIX=".dll"
elif [[ "$OSTYPE" == "freebsd"* ]]; then
    SO_SUFFIX=".so"
else
    SO_SUFFIX=".so"
fi


gen_soname () {
    # Arg 1 == name of library. eg "c"
    # Arg 2 == version. eg ".1.1.0"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo lib${1}${2}${SO_SUFFIX}
    else
        echo lib${1}${SO_SUFFIX}${2}
    fi
}

#echo $(gen_soname thing 2.2.2)
