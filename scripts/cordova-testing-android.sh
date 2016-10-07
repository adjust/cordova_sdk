#!/usr/bin/env bash

# Exit if any errors occur
set -e

SDK_NAME=com.adjust.sdk
SDK_DIR=~/Dev/cordova_sdk
SAMPLE_DIR=~/Dev/MyApp

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

echo -e "${GREEN}>>> Running Android build script ${NC}"
cd ${SDK_DIR}
ext/Android/build.sh; \cp -v ext/Android/adjust*.jar src/Android/adjust-android.jar

echo -e "${GREEN}>>> Re-installing cordova plugin ${NC}"
cd ${SAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin add ${SDK_DIR}

echo -e "${GREEN}>>> Running Cordova build Android ${NC}"
cordova build android

echo -e "${GREEN}>>> Build successful. Installing APK on device ${NC}"
cordova run android --device
