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

echo -e "${GREEN}>>> Running iOS build script ${NC}"
cd ${SDK_DIR}
ext/iOS/build.sh

echo -e "${GREEN}>>> Re-installing cordova plugin ${NC}"
cd ${SDK_DIR}/${SAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin add ${SDK_DIR}

echo -e "${GREEN}>>> Running Cordova build iOS ${NC}"
cordova build ios --device

echo -e "${GREEN}>>> Build successful. Installing IPA on device ${NC}"
#cordova run ios --device
