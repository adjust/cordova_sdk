#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Get absolute path
pushd `dirname $0` > /dev/null
ABS_ROOT_DIR=`cd ../..; pwd`
popd > /dev/null

# Relative directories
SRC_DIR=ext/iOS/sdk
LIB_OUT_DIR=src/iOS
SCRIPT_DIR=scripts

cd ${ABS_ROOT_DIR}
echo ">>><<<"
echo ">>> Removing old framework"
echo ">>><<<"
rm -rfv ${LIB_OUT_DIR}/AdjustSdk.framework

echo ">>><<<"
echo ">>> building new framework"
echo ">>><<<"
cd ${ABS_ROOT_DIR}/${SRC_DIR}
xcodebuild -target AdjustStatic -configuration Release

echo ">>><<<"
echo ">>> Copy built framework to designated location"
echo ">>><<<"
cd ${ABS_ROOT_DIR}
\cp -Rv ${SRC_DIR}/Frameworks/Static/AdjustSdk.framework ${LIB_OUT_DIR}

echo ">>><<<"
echo ">>> Running symlink fix"
echo ">>><<<"
${SCRIPT_DIR}/symlink_fix.sh
