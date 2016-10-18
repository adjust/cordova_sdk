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

echo -e "${GREEN}>>> Running Android build script ${NC}"
cd ${SDK_DIR}
ext/Android/build.sh; \cp -v ext/Android/adjust*.jar src/Android/adjust-android.jar

echo -e "${GREEN}>>> Running iOS build script ${NC}"
cd ${SDK_DIR}
ext/iOS/build.sh

echo -e "${GREEN}>>> Re-installing cordova plugin ${NC}"
cd ${SDK_DIR}/${SAMPLE_DIR}
cordova plugin add ${SDK_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add cordova-plugin-device
cordova plugin add cordova-universal-links-plugin

echo -e "${GREEN}>>> Running Cordova build Android ${NC}"
cordova build android

echo -e "${GREEN}>>> Running Cordova build iOS ${NC}"
cordova build ios --device

echo -e "${GREEN}>>> Build successful. Installing APK on device ${NC}"
#cordova run android --device --nobuild
