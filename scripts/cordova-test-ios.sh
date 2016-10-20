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

echo "${GREEN}>>> Updating git submodules ${NC}"
cd ${SDK_DIR}
git submodule update --init --recursive

echo "${GREEN}>>> Running iOS build script ${NC}"
ext/iOS/build.sh

echo "${GREEN}>>> Installing iOS platform ${NC}"
cd ${SDK_DIR}/${SAMPLE_DIR}
cordova platform add ios

echo "${GREEN}>>> Re-installing plugins ${NC}"
cordova plugin remove ${SDK_NAME}

cordova plugin add ${SDK_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme --variable URL_SCHEME=adjustExample
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add https://github.com/apache/cordova-plugin-device.git
cordova plugin add cordova-universal-links-plugin

echo "${GREEN}>>> Running Cordova build iOS ${NC}"
cordova build ios --device

echo "${GREEN}>>> Build successful. Installing APK on device ${NC}"
cordova run ios --device --nobuild
