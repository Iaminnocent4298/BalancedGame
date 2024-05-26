#!/bin/bash
set -e

if [ ! -d dist/ ]; then
    mkdir dist/
fi
if [ ! -d dist/com/ ]; then
    cp -r java/com dist/com
fi
rm -f dist/*.java dist/*.class
cp java/*.java dist/
cp java/*.json dist/
cd dist/
echo 'Compiling...'
javac BalancedGame.java