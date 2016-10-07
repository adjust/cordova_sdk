#!/usr/bin/env bash

# Exit if any errors occur
set -e

SDK_NAME=com.adjust.sdk
SDK_DIR=~/Dev/cordova_sdk
SAMPLE_DIR=~/Dev/MyApp

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

echo -e "${GREEN}>>> Running iOS build script ${NC}"
cd ${SDK_DIR}
ext/iOS/build.sh

echo -e "${GREEN}>>> Re-installing cordova plugin ${NC}"
cd ${SAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin add ${SDK_DIR}

echo -e "${GREEN}>>> Running Cordova build iOS ${NC}"
cordova build ios

echo -e "${GREEN}>>> Build successful. Installing IPA on device ${NC}"
cordova run ios --device
