SRCDIR = ./sdk
LIBOUTDIR = ../../src/iOS/
SCRIPTDIR = ../../scripts/

default: copy

build:	
	cd $(SRCDIR); \
	xcodebuild -target AdjustStatic -configuration Release

copy: build
	rm -rf $(LIBOUTDIR)/AdjustSdk.framework; \
	cp -R $(SRCDIR)/Frameworks/Static/AdjustSdk.framework $(LIBOUTDIR); \
        sh $(SCRIPTDIR)/symlink_fix.sh
