function AdjustConfig(appToken, environment) {
    
    // Adjust callbacks
    this.attributionChangedCallback = null;
    this.eventTrackingSucceededCallback = null;
    this.eventTrackingFailedCallback = null;
    this.sessionTrackingSucceededCallback = null;
    this.sessionTrackingFailedCallback = null;
    this.deferredDeeplinkReceivedCallback = null;
    this.skanConversionDataUpdatedCallback = null;

    //  Common configuration fields
    this.appToken = appToken;
    this.environment = environment;
    this.isSendingInBackgroundEnabled = null;
    this.isCostDataInAttributionEnabled = null;
    this.isDeviceIdsReadingOnceEnabled = null;
    // TODO: Implement the following 3 fields
    //this.urlStrategyDomains = null;
    //this.useSubdomains = null;
    //this.isDataResidency = null;
    this.sdkPrefix = null;
    this.logLevel = null;
    this.defaultTracker = null;
    this.externalDeviceId = null;
    this.eventDeduplicationIdsMaxSize = null;
    this.shouldLaunchDeeplink = null;

    // Android only
    this.referrer = null;
    this.processName = null;
    this.preinstallTrackingEnabled = null;
    this.preinstallFilePath = null;
    this.oaidReadingEnabled = null;
    this.playStoreKidsAppEnabled = null;
    this.finalAndroidAttributionEnabled = null;
    this.fbAppId = null;
    // iOS only 
    this.isAdServicesEnabled = null;
    this.isIdfaReadingEnabled = null;
    this.isIdfvReadingEnabled = null;
    this.isSkanAttributionEnabled = null;
    this.isLinkMeEnabled = null;
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


    // Adjust Callbacks
    AdjustConfig.prototype.getAttributionChangedCallback = function() {
        return this.attributionChangedCallback;
    };
    AdjustConfig.prototype.setAttributionChangedCallbackListener = function(callback) {
        this.attributionChangedCallback = callback;
    };
    AdjustConfig.prototype.hasAttributionChangedListener = function() {
        return this.attributionChangedCallback !== null;
    };


    AdjustConfig.prototype.getEventTrackingSucceededCallback = function() {
        return this.eventTrackingSucceededCallback;
    };
    AdjustConfig.prototype.setEventTrackingSucceededCallbackListener = function(eventTrackingSucceededCallback) {
        this.eventTrackingSucceededCallback = eventTrackingSucceededCallback;
    };
    AdjustConfig.prototype.hasEventTrackingSucceededListener = function() {
        return this.eventTrackingSucceededCallback !== null;
    };


    AdjustConfig.prototype.getEventTrackingFailedCallback = function() {
        return this.eventTrackingFailedCallback;
    };
    AdjustConfig.prototype.setEventTrackingFailedCallbackListener = function(eventTrackingFailedCallback) {
        this.eventTrackingFailedCallback = eventTrackingFailedCallback;
    };
    AdjustConfig.prototype.hasEventTrackingFailedListener = function() {
        return this.eventTrackingFailedCallback !== null;
    };


    AdjustConfig.prototype.getSessionTrackingSucceededCallback = function() {
        return this.sessionTrackingSucceededCallback;
    };
    AdjustConfig.prototype.setSessionTrackingSucceededCallbackListener = function(sessionTrackingSucceededCallback) {
        this.sessionTrackingSucceededCallback = sessionTrackingSucceededCallback;
    };
    AdjustConfig.prototype.hasSessionTrackingSucceededListener = function() {
        return this.sessionTrackingSucceededCallback !== null;
    };

    AdjustConfig.prototype.getSessionTrackingFailedCallback = function() {
        return this.sessionTrackingFailedCallback;
    };
    AdjustConfig.prototype.setSessionTrackingFailedCallbackListener = function(sessionTrackingFailedCallback) {
        this.sessionTrackingFailedCallback = sessionTrackingFailedCallback;
    };
    AdjustConfig.prototype.hasSessionTrackingFailedListener = function() {
        return this.sessionTrackingFailedCallback !== null;
    };

    AdjustConfig.prototype.getDeferredDeeplinkReceivedCallback = function() {
        return this.deferredDeeplinkReceivedCallback;
    };
    AdjustConfig.prototype.setDeferredDeeplinkReceivedCallbackListener = function(callback) {
        this.deferredDeeplinkReceivedCallback = callback;
    };
    AdjustConfig.prototype.hasDeferredDeeplinkReceivedCallbackListener = function() {
        return this.deferredDeeplinkReceivedCallback !== null;
    };

    AdjustConfig.prototype.getSkanConversionDataUpdatedCallback = function() {
        return this.skanConversionDataUpdatedCallback;
    };
    AdjustConfig.prototype.setSkanConversionDataUpdatedCallbackListener = function(callback) {
        this.skanConversionDataUpdatedCallback = callback;
    };
    AdjustConfig.prototype.hasSkanConversionDataUpdatedCallbackListener = function() {
        return this.skanConversionDataUpdatedCallback !== null;
    };

    // Configuration fields - common
    AdjustConfig.prototype.enableSendingInBackground = function() {
        this.isSendingInBackgroundEnabled = true;
    };

    AdjustConfig.prototype.enableCostDataInAttribution = function() {
        this.isCostDataInAttributionEnabled = true;
    };

    AdjustConfig.prototype.enableDeviceIdsReadingOnce = function () {
        this.isDeviceIdsReadingOnceEnabled = true;
    };

    AdjustConfig.prototype.setSdkPrefix = function(sdkPrefix) {
        this.sdkPrefix = sdkPrefix;
    };

    AdjustConfig.prototype.setLogLevel = function(logLevel) {
        this.logLevel = logLevel;
    };

    AdjustConfig.prototype.setDefaultTracker = function(defaultTracker) {
        this.defaultTracker = defaultTracker;
    };

    AdjustConfig.prototype.setExternalDeviceId = function(externalDeviceId) {
        this.externalDeviceId = externalDeviceId;
    };

    AdjustConfig.prototype.setEventDeduplicationIdsMaxSize = function(eventDeduplicationIdsnMaxSize) {
        this.eventDeduplicationIdsMaxSize = eventDeduplicationIdsnMaxSize;
    };

    AdjustConfig.prototype.setShouldLaunchDeeplink = function(shouldLaunchDeeplink) {
        this.shouldLaunchDeeplink = shouldLaunchDeeplink;
    };

    // Configuration fields - Android only
    // TODO: Should we keep the getter?
    AdjustConfig.prototype.getReferrer = function() {
        return this.referrer;
    };
    AdjustConfig.prototype.setReferrer = function(referrer) {
        this.referrer = referrer;
    };

    AdjustConfig.prototype.setProcessName = function(processName) {
        this.processName = processName;
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

    AdjustConfig.prototype.setPlayStoreKidsAppEnabled = function (isEnabled) {
        this.playStoreKidsAppEnabled = isEnabled;
    };

    AdjustConfig.prototype.setFinalAndroidAttributionEnabled = function (isEnabled) {
        this.finalAndroidAttributionEnabled = isEnabled;
    };

    AdjustConfig.prototype.setFbAppId = function (fbAppId) {
        this.fbAppId = fbAppId;
    };

    // Configuration fields - iOS only
    AdjustConfig.prototype.disableAdServices = function() {
        this.isAdServicesEnabled = false;
    };

    AdjustConfig.prototype.disableIdfaReading = function() {
        this.isIdfaReadingEnabled = false;
    };

    AdjustConfig.prototype.disableIdfvReading = function() {
        this.isIdfvReadingEnabled = false;
    };

    AdjustConfig.prototype.disableSkanAttribution = function() {
        this.isSkanAttributionEnabled = false;
    };

    AdjustConfig.prototype.enableLinkMe = function() {
        this.isLinkMeEnabled = true;
    };

    AdjustConfig.prototype.setAttConsentWaitingInterval = function(attConsentWaitingInterval) {
        this.attConsentWaitingInterval = attConsentWaitingInterval;
    };

    module.exports = AdjustConfig;
