#!/usr/bin/env bash
# - Build the JAR file by running the following Gradle tasks
#   - clean
#   - clearJar
#   - makeJar
# - Copy the JAR file to the root dir


BUILD_DIR=./sdk/Adjust
OUT_DIR=./adjust/build/outputs

# cd to the called directory to be able to run the script from anywhere
cd $(dirname $0) 

cd $BUILD_DIR
./gradlew clean makeReleaseJar
mv -v ${OUT_DIR}/*.jar ../../../../src/Android/adjust-android.jar
