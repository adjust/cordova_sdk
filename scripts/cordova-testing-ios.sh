#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Get absolute path
pushd `dirname $0` > /dev/null
ABS_ROOT_DIR=`cd ..; pwd`
popd > /dev/null

# Relative directories
# TODO: change to relative path and name it `sample`
SAMPLE_DIR=~/Dev/MyApp

SDK_NAME=com.adjust.sdk

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

cd ${ABS_ROOT_DIR}
ext/iOS/build.sh

cd ${SAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin add ${ABS_ROOT_DIR}
echo `pwd`
cordova build ios
cordova run ios --device
