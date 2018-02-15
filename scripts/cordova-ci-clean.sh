#!/usr/bin/env bash

# Exit if any errors occur
# set -e

# Get the current directory (/scripts/ directory)
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"
EXAMPLE_DIR=test_app
SDK_NAME=com.adjust.sdk
TEST_PLUGIN_NAME=com.adjust.sdktesting

RED='\033[0;31m' 	# Red color
GREEN='\033[0;32m' 	# Green color
NC='\033[0m' 		# No Color

echo -e "${GREEN}>>> Removing cordova plugins ${NC}"
cd ${ROOT_DIR}/${EXAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin rm ${TEST_PLUGIN_NAME}

rm -rfv plugins/com.adjust.sdk
rm -rfv plugins/com.adjust.sdktesting

cordova platform remove ios
cordova platform remove android
