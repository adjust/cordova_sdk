OUTDIR ?= .
SOURCEDIR = ./src/AdjustExtension
SOURCEDIRSDK = ./sdk
CC = xcodebuild

all:
	cd $(SOURCEDIRSDK); \
        rm -rf Frameworks/Static/*; \
	xcodebuild -target AdjustStatic -configuration Release;
	#mkdir -p AdjustSdk.framework/tmp; \
	#cp Frameworks/Static/AdjustSdk.framework/Versions/A/Headers/* AdjustSdk.framework/tmp; \
	#cp Frameworks/Static/AdjustSdk.framework/Versions/A/AdjustSdk AdjustSdk.framework/tmp; \
	#mkdir -p AdjustSdk.framework/Headers; \
	#mv AdjustSdk.framework/tmp/*.h AdjustSdk.framework/Headers; \
	#mv AdjustSdk.framework/tmp/AdjustSdk AdjustSdk.framework/AdjustSdk; \
	#rm -rf AdjustSdk.framework/tmp; cd ..; \
	#cp -R $(SOURCEDIRSDK)/AdjustSdk.framework $(SOURCEDIR)/include/Adjust/; \
	#cp -R $(SOURCEDIRSDK)/AdjustSdk.framework $(OUTDIR); \
	#rm -rf $(SOURCEDIRSDK)/AdjustSdk.framework
	#cd $(SOURCEDIR); $(CC) CONFIGURATION_BUILD_DIR=$(OUTDIR); \


clean:
	rm -rf $(SOURCEDIR)/build
