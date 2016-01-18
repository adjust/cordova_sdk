
#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Go to framework folder
cd ../../src/iOS/AdjustSdk.framework

# Remove any existing symlinks
rm -rf AdjustSdk
rm -rf Headers

# Move library and headers
mv Versions/A/AdjustSdk .
mv Versions/A/Headers .

# Remove Versions folder
rm -rf Versions
