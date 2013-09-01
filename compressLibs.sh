#!/bin/bash

libFolder="lib"
zipFileName="libs_jomp16-bot-31-08-2013.zip"

if [[ ! -d "$libFolder" ]]; then
    echo "Directory doesn't exists..."
    exit
fi

echo "Compressing libraries to $zipFileName"

cd "$libFolder"

find . -name "*.jar" -not -name "*-sources.jar" -print | zip -@ "../$zipFileName"

echo "Compressed!"
