#!/usr/bin/env bash

# End script if one of the lines fails
 set -e

# Get the current directory (ext/android/)
ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Traverse up to get to the root directory
ROOT_DIR="$(dirname "$ROOT_DIR")"
ROOT_DIR="$(dirname "$ROOT_DIR")"
BUILD_DIR=sdk/Adjust
JAR_OUT_DIR=test_plugin/src/Android

RED='\033[0;31m' # Red color
GREEN='\033[0;32m' # Green color
NC='\033[0m' # No Color

# cd to the called directory to be able to run the script from anywhere
cd $(dirname $0) 

cd $BUILD_DIR

JAR_IN_DIR=ext/android/sdk/Adjust/testlibrary/build/intermediates/bundles/debug
echo -e "${GREEN}>>> Running Gradle tasks: makeDebugJar${NC}"
./gradlew clean :testlibrary:makeJar

echo -e "${GREEN}>>> Moving the jar from ${JAR_IN_DIR} to ${JAR_OUT_DIR} ${NC}"
cd ${ROOT_DIR}
mv -v ${JAR_IN_DIR}/*.jar ${ROOT_DIR}/${JAR_OUT_DIR}/adjust-testing.jar
