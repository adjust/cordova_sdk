#!/usr/bin/env bash
# - Build the JAR file by running the following Gradle tasks
#   - clean
#   - clearJar
#   - makeJar
# - Copy the JAR file to the root dir


BUILD_DIR=./sdk/Adjust/
OUT_DIR=./adjust/build/outputs

cd $BUILD_DIR
./gradlew clean clearJar makeJar
mv -v ${OUT_DIR}/*.jar ../../
