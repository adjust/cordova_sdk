#!/usr/bin/env bash

# Exit if any errors occur
#set -e

# Get the current directory (/scripts/ directory)
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Traverse up to get to the root directory
SDK_DIR="$(dirname "$SCRIPTS_DIR")"
SAMPLE_DIR=sample
SDK_NAME=com.adjust.sdk

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

echo -e "${GREEN}>>> Updating git submodules ${NC}"
cd ${SDK_DIR}
git submodule update --init --recursive

echo -e "${GREEN}>>> Running Android build script ${NC}"
cd ${SDK_DIR}
ext/Android/build.sh; \cp -v ext/Android/adjust*.jar src/Android/adjust-android.jar

echo -e "${GREEN}>>> Installing Android platform ${NC}"
cd ${SDK_DIR}/${SAMPLE_DIR}
cordova platform remove android
cordova platform add android

echo -e "${GREEN}>>> Re-installing plugins ${NC}"
cordova plugin remove ${SDK_NAME}

cordova plugin add ${SDK_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme --variable URL_SCHEME=adjustExample
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add https://github.com/apache/cordova-plugin-device.git
cordova plugin add cordova-universal-links-plugin

echo -e "${GREEN}>>> Running Cordova build Android ${NC}"
cordova run android

#echo -e "${GREEN}>>> Build successful. APK generated ${NC}"
#echo -e "${GREEN}>>> Build successful. Installing APK on device ${NC}"
#cordova run android --device --nobuild
