#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Get the current directory
ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$ROOT_DIR")"
ROOT_DIR="$(dirname "$ROOT_DIR")"

TEST_LIBRARY_PROJECT_DIR=ext/ios/sdk/AdjustTests/AdjustTestLibrary
LIB_OUT_DIR=test/plugin/src/ios
FRAMEWORKS_DIR=ext/ios/sdk/Frameworks/Static/

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

cd ${ROOT_DIR}
echo -e "${GREEN}>>> Removing old framework ${NC}"
rm -rfv ${LIB_OUT_DIR}/AdjustTestLibrary.framework
echo Success

echo -e "${GREEN}>>> building new framework ${NC}"
cd ${ROOT_DIR}/${TEST_LIBRARY_PROJECT_DIR}/
xcodebuild -target AdjustTestLibraryStatic -configuration Debug clean build
echo Success

echo -e "${GREEN}>>> Copy built framework to designated location ${NC}"
cd ${ROOT_DIR}/${FRAMEWORKS_DIR}/
\cp -Rv AdjustTestLibrary.framework ${ROOT_DIR}/${LIB_OUT_DIR}
echo Success
