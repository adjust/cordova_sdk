#!/usr/bin/env bash

# Exit if any errors occur

# set -e

# Get the current directory (/scripts/ directory)
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"
PLUGIN_DIR=plugin
EXAMPLE_DIR=example
SDK_NAME=com.adjust.sdk

RED='\033[0;31m' 	# Red color
GREEN='\033[0;32m' 	# Green color
NC='\033[0m' 		# No Color

#echo -e "${GREEN}>>> Updating git submodules ${NC}"
#cd ${ROOT_DIR}
#git submodule update --init --recursive

echo -e "${GREEN}>>> Running iOS build script ${NC}"
ext/iOS/build.sh

echo -e "${GREEN}>>> Installing iOS platform ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova platform add ios@4.3.1

echo -e "${GREEN}>>> Re-installing plugins ${NC}"
cordova plugin remove ${SDK_NAME}

cordova plugin add ../${PLUGIN_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme --variable URL_SCHEME=adjustExample
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add https://github.com/apache/cordova-plugin-device.git
#cordova plugin add cordova-universal-links-plugin@https://github.com/aramando/cordova-universal-links-plugin.git#35b3ed7e9a0310b12f1ac92a5159b21ce50eee57

#echo -e "${GREEN}>>> Running Cordova build iOS ${NC}"
#cordova build ios --device

echo -e "${GREEN}>>> Build & Running  ${NC}"
cordova build ios
