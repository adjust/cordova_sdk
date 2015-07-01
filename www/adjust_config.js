function AdjustConfig(appToken, environment) {
    // iOS & Android
    this.appToken = appToken;
    this.environment = environment;

    this.sdkPrefix = "cordova4.0.0";

    this.logLevel = null;
    this.defaultTracker = null;
    this.callbackListener = null;

    this.eventBufferingEnabled = "NO";

    // iOS only
    this.macMd5TrackingEnabled = "NO";

    // Android only
    this.processName = null;
}

AdjustConfig.prototype.getAttributionCallback = function() {
    return this.callbackListener;
};

AdjustConfig.prototype.setEventBufferingEnabled = function(isEnabled) {
    if (isEnabled) {
        this.eventBufferingEnabled = "YES";
    } else {
        this.eventBufferingEnabled = "NO";
    }
};

AdjustConfig.prototype.setMacMd5TrackingEnabled = function(isEnabled) {
    if (isEnabled) {
        this.macMd5TrackingEnabled = "YES";
    } else {
        this.macMd5TrackingEnabled = "NO";
    }
};

AdjustConfig.prototype.setLogLevel = function(logLevel) {
    this.logLevel = logLevel;
};

AdjustConfig.prototype.setSdkPrefix = function(sdkPrefix) {
    this.sdkPrefix = sdkPrefix;
};

AdjustConfig.prototype.setProcessName = function(processName) {
    this.processName = processName;
};

AdjustConfig.prototype.setDefaultTracker = function(defaultTracker) {
    this.defaultTracker = defaultTracker;
};

AdjustConfig.prototype.setCallbackListener = function(callbackListener) {
    this.callbackListener = callbackListener;
};

AdjustConfig.prototype.isValid = function() {
    return this.appToken != null;
};

AdjustConfig.prototype.hasListener = function() {
    return this.callbackListener != null;
};

module.exports = AdjustConfig;