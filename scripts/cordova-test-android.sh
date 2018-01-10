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

echo -e "${GREEN}>>> Removing app from test device ${NC}"
adb uninstall com.adjust.examples || true

echo -e "${GREEN}>>> Running Android build script ${NC}"
cd ${ROOT_DIR}
ext/Android/build.sh release

echo -e "${GREEN}>>> Packaging plugin content to custom directory ${NC}"
cd ${ROOT_DIR}
rm -rf $TEMP_PLUGIN_DIR; mkdir $TEMP_PLUGIN_DIR
rsync -a . $TEMP_PLUGIN_DIR --exclude=example --exclude=ext --exclude=scripts --exclude=doc
echo Success

echo -e "${GREEN}>>> Installing Android platform ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova platform add android || true

echo -e "${GREEN}>>> Re-installing plugins ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova plugin remove ${SDK_NAME} || true

cordova plugin add ../${TEMP_PLUGIN_DIR}
cordova plugin add cordova-plugin-console
cordova plugin add cordova-plugin-customurlscheme --variable URL_SCHEME=adjustExample
cordova plugin add cordova-plugin-dialogs
cordova plugin add cordova-plugin-whitelist
cordova plugin add https://github.com/apache/cordova-plugin-device.git
cordova plugin add cordova-universal-links-plugin

echo -e "${GREEN}>>> Building cordova project ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova run android

echo -e "${GREEN}>>> Build successful. ${NC}"
