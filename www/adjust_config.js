function AdjustConfig(appToken, environment) {
    // Adjust callbacks
    this.attributionCallback = null;
    this.eventTrackingSucceededCallback = null;
    this.eventTrackingFailedCallback = null;
    this.sessionTrackingSucceededCallback = null;
    this.sessionTrackingFailedCallback = null;
    this.deferredDeeplinkCallback = null;
    this.skanUpdatedCallback = null;

    // Common configuration fields
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
    this.isFirstSessionDelayEnabled = null;
    this.storeInfo = null;

    // Android only
    this.processName = null;
    this.isPreinstallTrackingEnabled = null;
    this.preinstallFilePath = null;
    this.isPlayStoreKidsComplianceEnabled = null;
    this.fbAppId = null;
    // iOS only 
    this.isAdServicesEnabled = null;
    this.isIdfaReadingEnabled = null;
    this.isIdfvReadingEnabled = null;
    this.isSkanAttributionEnabled = null;
    this.isLinkMeEnabled = null;
    this.attConsentWaitingInterval = null;
    this.isAppTrackingTransparencyUsageEnabled = null;
}

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
    if (typeof sdkPrefix !== 'string') {
        console.log("[Adjust] SDK prefix is not of type string");
        return;
    }
    this.sdkPrefix = sdkPrefix;
};

AdjustConfig.prototype.setLogLevel = function(logLevel) {
    if (typeof logLevel !== 'string') {
        console.log("[Adjust] Log level is not of type string");
        return;
    }
    this.logLevel = logLevel;
};

AdjustConfig.prototype.setDefaultTracker = function(defaultTracker) {
    if (typeof defaultTracker !== 'string') {
        console.log("[Adjust] Default tracker is not of type string");
        return;
    }
    this.defaultTracker = defaultTracker;
};

AdjustConfig.prototype.setExternalDeviceId = function(externalDeviceId) {
    if (typeof externalDeviceId !== 'string') {
        console.log("[Adjust] External device ID is not of type string");
        return;
    }
    this.externalDeviceId = externalDeviceId;
};

AdjustConfig.prototype.setEventDeduplicationIdsMaxSize = function(eventDeduplicationIdsMaxSize) {
    if (!Number.isInteger(eventDeduplicationIdsMaxSize)) {
        console.log("[Adjust] Maximum number of event deduplication IDs is not of type integer");
        return;
    }
    this.eventDeduplicationIdsMaxSize = eventDeduplicationIdsMaxSize;
};

AdjustConfig.prototype.disableDeferredDeeplinkOpening = function() {
    this.isDeferredDeeplinkOpeningEnabled = false;
};

AdjustConfig.prototype.enableCoppaCompliance = function() {
    this.isCoppaComplianceEnabled = true;
};

AdjustConfig.prototype.enableFirstSessionDelay = function() {
    this.isFirstSessionDelayEnabled = true;
};

AdjustConfig.prototype.setStoreInfo = function(storeInfo) {
    this.storeInfo = storeInfo;
}

AdjustConfig.prototype.setProcessName = function(processName) {
    this.processName = processName;
};

AdjustConfig.prototype.enablePreinstallTracking = function() {
    this.isPreinstallTrackingEnabled = true;
};

AdjustConfig.prototype.setPreinstallFilePath = function(preinstallFilePath) {
    if (typeof preinstallFilePath !== 'string') {
        console.log("[Adjust] Preinstall file path is not of type string");
        return;
    }
    this.preinstallFilePath = preinstallFilePath;
};

AdjustConfig.prototype.enablePlayStoreKidsCompliance = function () {
    this.isPlayStoreKidsComplianceEnabled = true;
};

AdjustConfig.prototype.setFbAppId = function (fbAppId) {
    if (typeof fbAppId !== 'string') {
        console.log("[Adjust] FB app ID is not of type string");
        return;
    }
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
    if (!Number.isInteger(attConsentWaitingInterval)) {
        console.log("[Adjust] ATT consent waiting interval is not of type integer");
        return;
    }
    this.attConsentWaitingInterval = attConsentWaitingInterval;
};

AdjustConfig.prototype.setUrlStrategy = function(urlStrategyDomains, useSubdomains, isDataResidency) {
    if (!Array.isArray(urlStrategyDomains) ||
        typeof useSubdomains !== 'boolean' ||
        typeof isDataResidency !== 'boolean') {
        console.log("[Adjust] URL strategy parameters are not of a proper data types");
        return;
    }
    this.urlStrategyDomains = urlStrategyDomains;
    this.useSubdomains = useSubdomains;
    this.isDataResidency = isDataResidency;
};

AdjustConfig.prototype.disableAppTrackingTransparencyUsage = function() {
    this.isAppTrackingTransparencyUsageEnabled = false;
};

module.exports = AdjustConfig;
