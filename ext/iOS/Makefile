SRCDIR = ./sdk
LIBOUTDIR = ../../src/iOS/
SCRIPTDIR = ../../scripts/

default: copy

build:	
	cd $(SRCDIR); \
	xcodebuild -target AdjustStatic -configuration Release

copy: build
	rm -rf $(LIBOUTDIR)/AdjustSdk.framework; \
	cp -R $(SRCDIR)/AdjustSdk.framework $(LIBOUTDIR); \
	rm -rf $(SRCDIR)/AdjustSdk.framework; \
        sh $(SCRIPTDIR)/symlink_fix.sh
