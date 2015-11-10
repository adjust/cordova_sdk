function AdjustConfig(appToken, environment) {
    // iOS & Android
    this.appToken = appToken;
    this.environment = environment;

    this.sdkPrefix = "cordova4.1.2";

    this.logLevel = null;
    this.defaultTracker = null;
    this.callbackListener = null;

    this.eventBufferingEnabled = null;

    // iOS only
    this.macMd5TrackingEnabled = null;

    // Android only
    this.processName = null;
}

AdjustConfig.EnvironmentSandbox     = "sandbox";
AdjustConfig.EnvironmentProduction  = "production";

AdjustConfig.LogLevelVerbose        = "VERBOSE",
AdjustConfig.LogLevelDebug          = "DEBUG",
AdjustConfig.LogLevelInfo           = "INFO",
AdjustConfig.LogLevelWarn           = "WARN",
AdjustConfig.LogLevelError          = "ERROR",
AdjustConfig.LogLevelAssert         = "ASSERT",

AdjustConfig.prototype.getAttributionCallback = function() {
    return this.callbackListener;
};

AdjustConfig.prototype.setEventBufferingEnabled = function(isEnabled) {
    this.eventBufferingEnabled = isEnabled;
};

AdjustConfig.prototype.setMacMd5TrackingEnabled = function(isEnabled) {
    this.macMd5TrackingEnabled = isEnabled;
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

AdjustConfig.prototype.setCallbackListener = function(callbackListener) {
    this.callbackListener = callbackListener;
};

AdjustConfig.prototype.hasListener = function() {
    return this.callbackListener != null;
};

module.exports = AdjustConfig;
