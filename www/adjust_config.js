function AdjustConfig(appToken, environment) {
    
    // Adjust callbacks
    this.attributionCallback = null;
    this.eventTrackingSucceededCallback = null;
    this.eventTrackingFailedCallback = null;
    this.sessionTrackingSucceededCallback = null;
    this.sessionTrackingFailedCallback = null;
    this.deferredDeeplinkCallback = null;
    this.skanUpdatedCallback = null;

    //  Common configuration fields
    this.appToken = appToken;
    this.environment = environment;
    this.isSendingInBackgroundEnabled = null;
    this.isCostDataInAttributionEnabled = null;
    this.isDeviceIdsReadingOnceEnabled = null;
    this.urlStrategyDomains = null;
    this.useSubdomains = null;
    this.isDataResidency = null;
    this.sdkPrefix = null;
    this.logLevel = null;
    this.defaultTracker = null;
    this.externalDeviceId = null;
    this.eventDeduplicationIdsMaxSize = null;
    this.isDeferredDeeplinkOpeningEnabled = null;
    this.isCoppaComplianceEnabled = null;

    // Android only
    this.processName = null;
    this.isPreinstallTrackingEnabled = null;
    this.preinstallFilePath = null;
    this.isOaidReadingEnabled = null;
    this.isPlayStoreKidsAppEnabled = null;
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
    AdjustConfig.prototype.getAttributionCallback = function() {
        return this.attributionCallback;
    };
    AdjustConfig.prototype.setAttributionCallback = function(callback) {
        this.attributionCallback = callback;
    };
    AdjustConfig.prototype.hasAttributionCallback = function() {
        return this.attributionCallback !== null;
    };


    AdjustConfig.prototype.getEventTrackingSucceededCallback = function() {
        return this.eventTrackingSucceededCallback;
    };
    AdjustConfig.prototype.setEventTrackingSucceededCallback = function(callback) {
        this.eventTrackingSucceededCallback = callback;
    };
    AdjustConfig.prototype.hasEventTrackingSucceededCallback = function() {
        return this.eventTrackingSucceededCallback !== null;
    };


    AdjustConfig.prototype.getEventTrackingFailedCallback = function() {
        return this.eventTrackingFailedCallback;
    };
    AdjustConfig.prototype.setEventTrackingFailedCallback = function(callback) {
        this.eventTrackingFailedCallback = callback;
    };
    AdjustConfig.prototype.hasEventTrackingFailedCallback = function() {
        return this.eventTrackingFailedCallback !== null;
    };


    AdjustConfig.prototype.getSessionTrackingSucceededCallback = function() {
        return this.sessionTrackingSucceededCallback;
    };
    AdjustConfig.prototype.setSessionTrackingSucceededCallback = function(callback) {
        this.sessionTrackingSucceededCallback = callback;
    };
    AdjustConfig.prototype.hasSessionTrackingSucceededCallback = function() {
        return this.sessionTrackingSucceededCallback !== null;
    };


    AdjustConfig.prototype.getSessionTrackingFailedCallback = function() {
        return this.sessionTrackingFailedCallback;
    };
    AdjustConfig.prototype.setSessionTrackingFailedCallback = function(callback) {
        this.sessionTrackingFailedCallback = callback;
    };
    AdjustConfig.prototype.hasSessionTrackingFailedCallback = function() {
        return this.sessionTrackingFailedCallback !== null;
    };


    AdjustConfig.prototype.getDeferredDeeplinkCallback = function() {
        return this.deferredDeeplinkCallback;
    };
    AdjustConfig.prototype.setDeferredDeeplinkCallback = function(callback) {
        this.deferredDeeplinkCallback = callback;
    };
    AdjustConfig.prototype.hasDeferredDeeplinkCallback = function() {
        return this.deferredDeeplinkCallback !== null;
    };


    AdjustConfig.prototype.getSkanUpdatedCallback = function() {
        return this.skanUpdatedCallback;
    };
    AdjustConfig.prototype.setSkanUpdatedCallback = function(callback) {
        this.skanUpdatedCallback = callback;
    };
    AdjustConfig.prototype.hasSkanUpdatedCallback = function() {
        return this.skanUpdatedCallback !== null;
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

    AdjustConfig.prototype.setEventDeduplicationIdsMaxSize = function(eventDeduplicationIdsMaxSize) {
        this.eventDeduplicationIdsMaxSize = eventDeduplicationIdsMaxSize;
    };

    AdjustConfig.prototype.setDeferredDeeplinkOpeningEnabled = function(deferredDeeplinkOpeningEnabled) {
        this.isDeferredDeeplinkOpeningEnabled = deferredDeeplinkOpeningEnabled;
    };

    AdjustConfig.prototype.enableCoppaCompliance = function() {
        this.isCoppaComplianceEnabled = true;
    };

    AdjustConfig.prototype.setProcessName = function(processName) {
        this.processName = processName;
    };

    AdjustConfig.prototype.setPreinstallTrackingEnabled = function(isEnabled) {
        this.isPreinstallTrackingEnabled = isEnabled;
    };

    AdjustConfig.prototype.setPreinstallFilePath = function(preinstallFilePath) {
        this.preinstallFilePath = preinstallFilePath;
    };

    AdjustConfig.prototype.setOaidReadingEnabled = function(enableOaidReading) {
        this.isOaidReadingEnabled = enableOaidReading;
    };

    AdjustConfig.prototype.setPlayStoreKidsAppEnabled = function (isEnabled) {
        this.isPlayStoreKidsAppEnabled = isEnabled;
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

    AdjustConfig.prototype.setUrlStrategy = function(urlStrategyDomains, useSubdomains, isDataResidency) {
         this.urlStrategyDomains = urlStrategyDomains;
         this.useSubdomains = useSubdomains;
         this.isDataResidency = isDataResidency;
     };

    module.exports = AdjustConfig;
