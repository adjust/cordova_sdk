#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# get Absolute root path
pushd `dirname $0` > /dev/null
ABS_ROOT_DIR=`cd ..; pwd`
popd > /dev/null

# Relative directories
SRC_DIR=src/iOS

echo ">>><<<"
echo ">>> Symlink_fix started"
echo ">>><<<"

# Go to framework folder
cd ${ABS_ROOT_DIR}/${SRC_DIR}/AdjustSdk.framework

# Remove any existing symlinks
rm -rfv AdjustSdk
rm -rfv Headers

# Move library and headers
mv -v Versions/A/AdjustSdk .
mv -v Versions/A/Headers .

# Remove Versions folder
rm -rfv Versions

echo ">>><<<"
echo ">>> Symlink_fix finished"
echo ">>><<<"
