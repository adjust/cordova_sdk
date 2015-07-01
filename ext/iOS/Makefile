SRCDIR = ./sdk
LIBOUTDIR = ../../src/iOS/

default: copy

build:	
	cd $(SRCDIR); \
	xcodebuild -target framework -configuration Release

copy: build
	rm -rf $(LIBOUTDIR)/Adjust.framework; \
	cp -R $(SRCDIR)/Adjust.framework $(LIBOUTDIR); \
	rm -rf $(SRCDIR)/Adjust.framework
