function AdjustConfig(appToken, environment) {
    this.appToken = appToken;
    this.environment = environment;
    this.delayStart = 0.0;
    this.logLevel = null;
    this.userAgent = null;
    this.isDeviceKnown = null;
    this.defaultTracker = null;
    this.externalDeviceId = null;
    this.urlStrategy = null;
    this.sendInBackground = null;
    this.shouldLaunchDeeplink = null;
    this.eventBufferingEnabled = null;
    this.needsCost = null;
    this.attributionCallback = null;
    this.eventTrackingSucceededCallback = null;
    this.eventTrackingFailedCallback = null;
    this.sessionTrackingSucceededCallback = null;
    this.sessionTrackingFailedCallback = null;
    this.deferredDeeplinkCallback = null;
    this.conversionValueUpdatedCallback = null;
    this.skad4ConversionValueUpdatedCallback = null;
    this.coppaCompliantEnabled = null;
    this.sdkPrefix = null;
    this.secretId = null;
    this.info1 = null;
    this.info2 = null;
    this.info3 = null;
    this.info4 = null;
    // Android only
    this.referrer = null;
    this.processName = null;
    this.readMobileEquipmentIdentity = null;
    this.preinstallTrackingEnabled = null;
    this.preinstallFilePath = null;
    this.oaidReadingEnabled = null;
    this.playStoreKidsAppEnabled = null;
    this.finalAndroidAttributionEnabled = null;
    // iOS only 
    this.allowiAdInfoReading = null;
    this.allowAdServicesInfoReading = null;
    this.allowIdfaReading = null;
    this.handleSkAdNetwork = null;
    this.linkMeEnabled = null;
    this.attConsentWaitingInterval = null;
};

AdjustConfig.EnvironmentSandbox = "sandbox";
AdjustConfig.EnvironmentProduction = "production";

AdjustConfig.LogLevelVerbose = "VERBOSE";
AdjustConfig.LogLevelDebug = "DEBUG";
AdjustConfig.LogLevelInfo  = "INFO";
AdjustConfig.LogLevelWarn = "WARN";
AdjustConfig.LogLevelError = "ERROR";
AdjustConfig.LogLevelAssert = "ASSERT";
AdjustConfig.LogLevelSuppress = "SUPPRESS";

AdjustConfig.UrlStrategyChina = "china";
AdjustConfig.UrlStrategyIndia = "india";
AdjustConfig.UrlStrategyCn = "cn";

AdjustConfig.DataResidencyEU = "data-residency-eu";
AdjustConfig.DataResidencyTR = "data-residency-tr";
AdjustConfig.DataResidencyUS = "data-residency-us";

AdjustConfig.AdRevenueSourceAppLovinMAX = "applovin_max_sdk";
AdjustConfig.AdRevenueSourceMopub = "mopub";
AdjustConfig.AdRevenueSourceAdMob = "admob_sdk";
AdjustConfig.AdRevenueSourceIronSource = "ironsource_sdk";
AdjustConfig.AdRevenueSourceAdMost = "admost_sdk";
AdjustConfig.AdRevenueSourceUnity = "unity_sdk";
AdjustConfig.AdRevenueSourceHeliumChartboost = "helium_chartboost_sdk";
AdjustConfig.AdRevenueSourcePublisher = "publisher_sdk";

AdjustConfig.prototype.getUserAgent = function() {
    return this.userAgent;
};

AdjustConfig.prototype.getDelayStart = function() {
    return this.delayStart;
};

AdjustConfig.prototype.getReferrer = function() {
    return this.referrer;
};

AdjustConfig.prototype.getSendInBackground = function() {
    return this.sendInBackground;
};

AdjustConfig.prototype.getShouldLaunchDeeplink = function() {
    return this.shouldLaunchDeeplink;
};

AdjustConfig.prototype.getAttributionCallback = function() {
    return this.attributionCallback;
};

AdjustConfig.prototype.getEventTrackingSucceededCallback = function() {
    return this.eventTrackingSucceededCallback;
};

AdjustConfig.prototype.getEventTrackingFailedCallback = function() {
    return this.eventTrackingFailedCallback;
};

AdjustConfig.prototype.getSessionTrackingSucceededCallback = function() {
    return this.sessionTrackingSucceededCallback;
};

AdjustConfig.prototype.getSessionTrackingFailedCallback = function() {
    return this.sessionTrackingFailedCallback;
};

AdjustConfig.prototype.getDeferredDeeplinkCallback = function() {
    return this.deferredDeeplinkCallback;
};

AdjustConfig.prototype.getConversionValueUpdatedCallback = function() {
    return this.conversionValueUpdatedCallback;
};

AdjustConfig.prototype.getSkad4ConversionValueUpdatedCallback = function() {
    return this.skad4ConversionValueUpdatedCallback;
};

AdjustConfig.prototype.setEventBufferingEnabled = function(isEnabled) {
    this.eventBufferingEnabled = isEnabled;
};

AdjustConfig.prototype.setPreinstallTrackingEnabled = function(isEnabled) {
    this.preinstallTrackingEnabled = isEnabled;
};

AdjustConfig.prototype.setPreinstallFilePath = function(preinstallFilePath) {
    this.preinstallFilePath = preinstallFilePath;
};

AdjustConfig.prototype.setOaidReadingEnabled = function(enableOaidReading) {
    this.oaidReadingEnabled = enableOaidReading;
};

AdjustConfig.prototype.setNeedsCost = function(needsCost) {
    this.needsCost = needsCost;
};

AdjustConfig.prototype.setLogLevel = function(logLevel) {
    this.logLevel = logLevel;
};

AdjustConfig.prototype.setProcessName = function(processName) {
    this.processName = processName;
};

AdjustConfig.prototype.setDefaultTracker = function(defaultTracker) {
    this.defaultTracker = defaultTracker;
};

AdjustConfig.prototype.setExternalDeviceId = function(externalDeviceId) {
    this.externalDeviceId = externalDeviceId;
};

AdjustConfig.prototype.setUrlStrategy = function(urlStrategy) {
    this.urlStrategy = urlStrategy;
};

AdjustConfig.prototype.setUserAgent = function(userAgent) {
    this.userAgent = userAgent;
};

AdjustConfig.prototype.setDeviceKnown = function(isDeviceKnown) {
    this.isDeviceKnown = isDeviceKnown;
};

AdjustConfig.prototype.getSdkPrefix = function() {
    return this.sdkPrefix;
};

AdjustConfig.prototype.setSdkPrefix = function(sdkPrefix) {
    this.sdkPrefix = sdkPrefix;
};

// @deprecated
AdjustConfig.prototype.setAllowiAdInfoReading = function(allowiAdInfoReading) {
    console.warn("Calling deprecated function! Apple Search Ads attribution with usage of iAd.framework has been sunset by Apple as of February 7th 2023.");
    // this.allowiAdInfoReading = allowiAdInfoReading;
};

AdjustConfig.prototype.setAllowAdServicesInfoReading = function(allowAdServicesInfoReading) {
    this.allowAdServicesInfoReading = allowAdServicesInfoReading;
};

AdjustConfig.prototype.setAllowIdfaReading = function(allowIdfaReading) {
    this.allowIdfaReading = allowIdfaReading;
};

AdjustConfig.prototype.deactivateSKAdNetworkHandling = function() {
    this.handleSkAdNetwork = false;
};

AdjustConfig.prototype.setLinkMeEnabled = function(linkMeEnabled) {
    this.linkMeEnabled = linkMeEnabled;
};

AdjustConfig.prototype.setAttConsentWaitingInterval = function(attConsentWaitingInterval) {
    this.attConsentWaitingInterval = attConsentWaitingInterval;
};

// @deprecated
AdjustConfig.prototype.setReadMobileEquipmentIdentity = function(readMobileEquipmentIdentity) {
    console.warn("Calling deprecated function! This functionality has been removed from the SDK.");
    // this.readMobileEquipmentIdentity = readMobileEquipmentIdentity;
};

AdjustConfig.prototype.setAppSecret = function(secretId, info1, info2, info3, info4) {
    if (secretId !== null) {
        this.secretId = secretId.toString();
    }
    if (info1 !== null) {
        this.info1 = info1.toString();
    }
    if (info2 !== null) {
        this.info2 = info2.toString();
    }
    if (info3 !== null) {
        this.info3 = info3.toString();
    }
    if (info4 !== null) {
        this.info4 = info4.toString();
    }
};

AdjustConfig.prototype.setDelayStart = function(delayStart) {
    this.delayStart = delayStart;
};

AdjustConfig.prototype.setReferrer = function(referrer) {
    this.referrer = referrer;
};

AdjustConfig.prototype.setSendInBackground = function(sendInBackground) {
    this.sendInBackground = sendInBackground;
};

AdjustConfig.prototype.setShouldLaunchDeeplink = function(shouldLaunchDeeplink) {
    this.shouldLaunchDeeplink = shouldLaunchDeeplink;
};

// @deprecated
AdjustConfig.prototype.setCallbackListener = function(callbackListener) {
    console.warn("Calling deprecated function! Use the setAttributionCallbackListener instead. Check adjust_config.js for more info.");
    this.attributionCallback = callbackListener;
};

AdjustConfig.prototype.setAttributionCallbackListener = function(attributionCallback) {
    this.attributionCallback = attributionCallback;
};

AdjustConfig.prototype.setEventTrackingSucceededCallbackListener = function(eventTrackingSucceededCallback) {
    this.eventTrackingSucceededCallback = eventTrackingSucceededCallback;
};

AdjustConfig.prototype.setEventTrackingFailedCallbackListener = function(eventTrackingFailedCallback) {
    this.eventTrackingFailedCallback = eventTrackingFailedCallback;
};

AdjustConfig.prototype.setSessionTrackingSucceededCallbackListener = function(sessionTrackingSucceededCallback) {
    this.sessionTrackingSucceededCallback = sessionTrackingSucceededCallback;
};

AdjustConfig.prototype.setSessionTrackingFailedCallbackListener = function(sessionTrackingFailedCallback) {
    this.sessionTrackingFailedCallback = sessionTrackingFailedCallback;
};

AdjustConfig.prototype.setDeferredDeeplinkCallbackListener = function(deferredDeeplinkCallback) {
    this.deferredDeeplinkCallback = deferredDeeplinkCallback;
};

AdjustConfig.prototype.setConversionValueUpdatedCallbackListener = function(conversionValueUpdatedCallback) {
    this.conversionValueUpdatedCallback = conversionValueUpdatedCallback;
};

AdjustConfig.prototype.setSkad4ConversionValueUpdatedCallbackListener = function(skad4ConversionValueUpdatedCallback) {
    this.skad4ConversionValueUpdatedCallback = skad4ConversionValueUpdatedCallback;
};

// @deprecated
AdjustConfig.prototype.hasListener = function() {
    console.warn("Calling deprecated function! Use the hasAttributionListener instead. Check adjust_config.js for more info");
    return this.attributionCallback !== null;
};

AdjustConfig.prototype.hasAttributionListener = function() {
    return this.attributionCallback !== null;
};

AdjustConfig.prototype.hasEventTrackingSucceededListener = function() {
    return this.eventTrackingSucceededCallback !== null;
};

AdjustConfig.prototype.hasEventTrackingFailedListener = function() {
    return this.eventTrackingFailedCallback !== null;
};

AdjustConfig.prototype.hasSessionTrackingSucceededListener = function() {
    return this.sessionTrackingSucceededCallback !== null;
};

AdjustConfig.prototype.hasSessionTrackingFailedListener = function() {
    return this.sessionTrackingFailedCallback !== null;
};

AdjustConfig.prototype.hasDeferredDeeplinkCallbackListener = function() {
    return this.deferredDeeplinkCallback !== null;
};

AdjustConfig.prototype.hasConversionValueUpdatedCallbackListener = function() {
    return this.conversionValueUpdatedCallback !== null;
};

AdjustConfig.prototype.hasSkad4ConversionValueUpdatedCallbackListener = function() {
    return this.skad4ConversionValueUpdatedCallback !== null;
};

AdjustConfig.prototype.setCoppaCompliantEnabled = function (isEnabled) {
    this.coppaCompliantEnabled = isEnabled;
};

AdjustConfig.prototype.setPlayStoreKidsAppEnabled = function (isEnabled) {
    this.playStoreKidsAppEnabled = isEnabled;
};

AdjustConfig.prototype.setFinalAndroidAttributionEnabled = function (isEnabled) {
    this.finalAndroidAttributionEnabled = isEnabled;
};

module.exports = AdjustConfig;
