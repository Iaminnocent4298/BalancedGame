#!/bin/bash
set -e

if [ ! -d dist/ ]; then
    mkdir dist/
fi
rm -f dist/*.class
cp java/*.json dist/

cd java
echo 'Compiling...'
javac BalancedGame.java -d ../dist