#!/usr/bin/env bash

git submodule update --init

cp -r src/android/Adjust/Adjust/src/com/adjust/sdk/ lib/android/
cp -r src/ios/Adjust/Adjust/ lib/ios/
