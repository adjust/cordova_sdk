
#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Go to framework folder
cd ../../src/iOS/AdjustPurchaseSdk.framework

# Remove any existing symlinks
rm -rf AdjustPurchaseSdk
rm -rf Headers

# Move library and headers
mv Versions/A/AdjustPurchaseSdk .
mv Versions/A/Headers .

# Remove Versions folder
rm -rf Versions
