#!/bin/bash

zipFileName="libs_jomp16-bot-31-08-2013.zip"
urlBase="http://jomp16.tk/assets/"
libTmp="lib_tmp"
libFolder="lib"

if [[ -d "$libFolder" ]]; then
    echo "$libFolder exists, deleting..."
    rm -rf "$libFolder"
fi

if [[ -d "$libTmp" ]]; then
    echo "$libTmp exists, deleting..."
    rm -rf "$libTmp"
fi

echo "Downloading libraries to $libFolder"

mkdir "$libTmp"

cd "$libTmp"

echo "Downloading $urlBase$zipFileName..."

wget "$urlBase$zipFileName"

echo "Unziping $zipFileName to ../$libFolder..."

unzip "$zipFileName" -d "../$libFolder"

echo "Deleting $libTmp..."

cd ..

rm -rf "$libTmp"
