
#!/usr/bin/env bash

# End script if one of the lines fails
set -e

# Go to framework folder
cd ../../src/iOS/Adjust.framework

# Remove any existing symlinks
rm -rf Adjust
rm -rf Headers

# Move library and headers
mv Versions/A/Adjust .
mv Versions/A/Headers .

# Remove Versions folder
rm -rf Versions
