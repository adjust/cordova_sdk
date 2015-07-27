
#!/usr/bin/env bash

# Go to framework folder
cd plugins/com.adjust.sdk/src/iOS/Adjust.framework

# Remove any existing symlinks
rm -rf Adjust
rm -rf Headers

# Make new symlinks
ln -s Versions/A/Adjust Adjust
ln -s Versions/A/Headers Headers
