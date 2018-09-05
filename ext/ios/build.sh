#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Get the current directory
ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$ROOT_DIR")"
ROOT_DIR="$(dirname "$ROOT_DIR")"

EXT_DIR=ext/ios/sdk
LIB_OUT_DIR=src/ios
SCRIPTS_DIR=scripts

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

cd ${ROOT_DIR}
echo -e "${GREEN}>>> Removing old framework ${NC}"
rm -rfv ${LIB_OUT_DIR}/AdjustSdk.framework

echo -e "${GREEN}>>> building new framework ${NC}"
cd ${ROOT_DIR}/${EXT_DIR}
xcodebuild -target AdjustStatic -configuration Release clean build

echo -e "${GREEN}>>> Copy built framework to designated location ${NC}"
cd ${ROOT_DIR}
\cp -Rv ${EXT_DIR}/Frameworks/Static/AdjustSdk.framework ${LIB_OUT_DIR}

echo -e "${GREEN}>>> Running symlink fix ${NC}"
${SCRIPTS_DIR}/symlink_fix.sh
