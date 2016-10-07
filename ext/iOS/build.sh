#!/usr/bin/env bash

# End script if one of the lines fails
set -e


SDK_DIR=~/Dev/cordova_sdk
SRC_DIR=ext/iOS/sdk
LIB_OUT_DIR=src/iOS
SCRIPT_DIR=scripts

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

cd ${SDK_DIR}
echo -e "${GREEN}>>> Removing old framework ${NC}"
rm -rfv ${LIB_OUT_DIR}/AdjustSdk.framework

echo -e "${GREEN}>>> building new framework ${NC}"
cd ${SDK_DIR}/${SRC_DIR}
xcodebuild -target AdjustStatic -configuration Release

echo -e "${GREEN}>>> Copy built framework to designated location ${NC}"
cd ${SDK_DIR}
\cp -Rv ${SRC_DIR}/Frameworks/Static/AdjustSdk.framework ${LIB_OUT_DIR}

echo -e "${GREEN}>>> Running symlink fix ${NC}"
${SCRIPT_DIR}/symlink_fix.sh
