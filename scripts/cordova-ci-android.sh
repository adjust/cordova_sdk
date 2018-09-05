#!/usr/bin/env bash

# Exit if any errors occur
set -e

# Get the current directory (/scripts/ directory)
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"
PROJECT_DIR=test/app
SDK_PLUGIN_NAME=com.adjust.sdk
TESTING_PLUGIN_NAME=com.adjust.sdktesting
TESTING_PLUGIN_DIR=test/plugin
SCRIPTS_DIR=scripts
TEMP_PLUGIN_DIR=temp_plugin
TEST_APP_PACKAGE=com.adjust.testapp

RED='\033[0;31m' 	# Red color
GREEN='\033[0;32m' 	# Green color
NC='\033[0m' 		# No Color

echo -e ">>> START ${NC}"

echo -e "${GREEN}>>> Removing app from test device ${NC}"
adb uninstall ${TEST_APP_PACKAGE} || true

echo -e "${GREEN}>>> Running Android build script ${NC}"
cd ${ROOT_DIR}
ext/android/build.sh release
ext/android/build_test_lib.sh

echo -e "${GREEN}>>> Packaging plugin content to custom directory ${NC}"
cd ${ROOT_DIR}
rm -rf $TEMP_PLUGIN_DIR; mkdir $TEMP_PLUGIN_DIR
rsync -a . $TEMP_PLUGIN_DIR --exclude=example --exclude=ext --exclude=scripts --exclude=doc --exclude=test/app --exclude=test/plugin --exclude=temp_plugin --exclude=.git
echo Success

echo -e "${GREEN}>>> Installing Android platform ${NC}"
cd ${ROOT_DIR}/${PROJECT_DIR}
cordova platform add android@6.4.0 || true

echo -e "${GREEN}>>> Re-installing plugins${NC}"
cd ${ROOT_DIR}/${PROJECT_DIR}
cordova plugin remove ${SDK_PLUGIN_NAME} || true
cordova plugin remove ${TESTING_PLUGIN_NAME} || true

cordova plugin add --verbose ../../${TEMP_PLUGIN_DIR} --nofetch
cordova plugin add --verbose ../../${TESTING_PLUGIN_DIR} --nofetch
cordova plugin add --verbose cordova-plugin-device

echo -e "${GREEN}>>> Running Cordova build ${NC}"
cd ${ROOT_DIR}/${PROJECT_DIR}
cordova build android --verbose
adb install -r platforms/android/build/outputs/apk/debug/android-debug.apk
adb shell monkey -p ${TEST_APP_PACKAGE} 1

cd ${ROOT_DIR}
rm -rf $TEMP_PLUGIN_DIR

echo -e ">>> END ${NC}"
