SRCDIR = ./sdk
LIBOUTDIR = ../../src/iOS/
SCRIPTDIR = ../../scripts/

default: copy

build:	
	cd $(SRCDIR); \
	xcodebuild -target AdjustPurchaseStatic -configuration Release

copy: build
	rm -rf $(LIBOUTDIR)/AdjustPurchaseSdk.framework; \
	cp -R $(SRCDIR)/Frameworks/Static/AdjustPurchaseSdk.framework $(LIBOUTDIR); \
	sh $(SCRIPTDIR)/symlink_fix.sh
