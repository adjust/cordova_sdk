#!/usr/bin/env bash

# Exit if any errors occur
set -e

# Get the current directory (/scripts/ directory)
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"
TEMP_PLUGIN_DIR=temp_plugin
EXAMPLE_DIR=example
SDK_NAME=com.adjust.sdk

RED='\033[0;31m' 	# Red color
GREEN='\033[0;32m' 	# Green color
NC='\033[0m' 		# No Color

echo -e "${GREEN}>>> Running iOS build script ${NC}"
cd ${ROOT_DIR}
ext/iOS/build.sh

echo -e "${GREEN}>>> Packaging plugin content to custom directory ${NC}"
cd ${ROOT_DIR}
rm -rf $TEMP_PLUGIN_DIR; mkdir $TEMP_PLUGIN_DIR
rsync -a . $TEMP_PLUGIN_DIR --exclude=example --exclude=ext --exclude=scripts --exclude=doc
echo Success

echo -e "${GREEN}>>> Installing iOS platform ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova platform add ios || true

echo -e "${GREEN}>>> Re-installing plugins ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova plugin remove ${SDK_NAME} || true

cordova plugin add ../${TEMP_PLUGIN_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme --variable URL_SCHEME=adjustExample
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add https://github.com/apache/cordova-plugin-device.git
#cordova plugin add cordova-universal-links-plugin@https://github.com/aramando/cordova-universal-links-plugin.git#35b3ed7e9a0310b12f1ac92a5159b21ce50eee57

echo -e "${GREEN}>>> Build & Running  ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova build ios

echo -e "${GREEN}>>> Build successful. Run it from Xcode (platforms/ios/) ${NC}"
cd ${ROOT_DIR}
rm -rf $TEMP_PLUGIN_DIR
