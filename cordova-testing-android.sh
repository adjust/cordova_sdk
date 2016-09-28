#!/usr/bin/env bash

SDK_NAME=com.adjust.sdk
SDK_DIR=~/Dev/cordova_sdk
SAMPLE_DIR=~/Dev/MyApp

cd ${SDK_DIR}
ext/Android/build.sh; \cp -v ext/Android/adjust*.jar src/Android/adjust-android.jar

cd ${SAMPLE_DIR}
cordova plugin rm ${SDK_NAME}
cordova plugin add ${SDK_DIR}
cordova build android
adb install -r platforms/android/build/outputs/apk/android-debug.apk
