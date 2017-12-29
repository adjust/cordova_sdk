#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Go to root folder
cd ..

# Clean the folders
rm -rf Frameworks/Static
rm -rf Frameworks/Dynamic

# Create needed folders
mkdir -p Frameworks/Static
mkdir -p Frameworks/Dynamic

# Build static AdjustPurchaseSdk.framework
xcodebuild -target AdjustPurchaseStatic -configuration Release

# Build dynamic AdjustPurchaseSdk.framework
xcodebuild -target AdjustPurchaseSdk -configuration Release

# Build Carthage AdjustPurchaseSdk.framework
carthage build --no-skip-current

# Copy build Carthage framework to Frameworks/Dynamic folder
cp -R Carthage/Build/iOS/* Frameworks/Dynamic/
